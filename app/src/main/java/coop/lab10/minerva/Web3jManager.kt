package coop.lab10.minerva

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.uport.sdk.signer.UportHDSigner
import com.uport.sdk.signer.encryption.KeyProtection
import coop.lab10.minerva.events.ErrorEvent
import coop.lab10.minerva.features.services.PushNotificationService
import coop.lab10.minerva.web3.StreemableERC20Token
import org.greenrobot.eventbus.EventBus
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.crypto.WalletUtils
import org.web3j.utils.Convert
import org.web3j.tx.Transfer
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger

/*
TODO:
* - allow the App to have multiple instances (connected to various networks) in parallel
* - define RPC URL on init (constructor param)
*
* constructor(rpc,
*
* interface:
* - createWallet() - returns mnemonic
* - restoreWallet(mnemonic)
* - sendNativeToken(receiver, amount)
* - sendERC20Token(receiver, amount)
* Sent transactions should trigger 2 callbacks:
* 1) transaction hash - remote operation, but not blocking on remote. Not expected to fail (just the usual potential connection issues)
* 2) receipt - remote operation, blocking on remote until included in block. May fail (on contract level) or timeout.
*/



class Web3jManager {
    val TAG = Web3jManager::class.java.simpleName

    val web3j: Web3j
    val credentials: Credentials
    val transactionManager: TransactionManager
    val streemContract: StreemableERC20Token
    // this is ARTIS specific. TODO: create a dedicated ARTIS class and move it there
    val gasPrice = BigInteger.valueOf(1000000000)

    companion object {
        // TODO define proper polling time base on the block time. Default for Web3j is 15s
        const val DEFAULT_POLLING_FREQUENCY = 2 * 1000
    }

    init {
        Log.i(TAG, "Web3jManager init")

        web3j = Web3j.build(HttpService("https://rpc.tau1.artis.network"))
        // this return the private internal storage dir of the App
        //val dir = context.filesDir

        /*
        TODO: move to a config file which isn't in git, set to something strong.
        Password protecting the wallet / mnemonic file would make sense, even if the pw is stored alongside.
        That way a malware scanning a device for Ethereum keys / mnemonics couldn't use that info without extra work.
        Proposal: split password. One part hardcoded in APK, one part generated per device on first App start.
        That way an attacker would need to fetch the device specific part + figure out the APK part in order to unlock a stolen keystore/mnemonic.
        */
        val password = ""

        /*
        generateNewWalletFile() fails with NoSuchAlgorithmException.
        Since generateBip39Wallet() returns an mnemonic, we may may ignore the created wallet file altogether,
        manually store the mnemonic instead.
        That may make handling of restore less complex: ask the user for mnemonic, store it to file -> business as usual
        The wallet file should then probably be deleted.
        */

        //val fileName = WalletUtils.generateNewWalletFile(password, dir)
        /*val bip39Wallet = WalletUtils.generateBip39Wallet(password, dir)
        println("filename of new wallet is " + bip39Wallet.filename)
        println("mnemonic of new wallet is " + bip39Wallet.mnemonic)
        */

        // translates to address 0x29258491830579326f574fe1353169c533af2b55
        val mnemonic = "body east convince derive return aisle exhaust idle consider interest oyster effort"
        credentials = WalletUtils.loadBip39Credentials(password, mnemonic)

        Log.d(TAG, "my address: ${credentials.address}")

        transactionManager = RawTransactionManager(web3j, credentials, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, DEFAULT_POLLING_FREQUENCY)

        // SFAU token on tau1
        val contractAddr = "0xF6eF10E21166cf2e33DB070AFfe262F90365e8D4"
        // TODO: use non-deprecated variant of load()
        streemContract = StreemableERC20Token.load(contractAddr, web3j, transactionManager,
                gasPrice, BigInteger.valueOf(2000000))
    }


    /*
    transactions need to run outside the main (UI) thread, otherwise we get an NetworkOnMainThreadException.
    The callbacks however should be executed on the main thread.
    What's the best interface for achieving that?
    */
    fun sendNativeToken(receiver: String, amount: BigDecimal) {
        // TODO: check if web3j supports estimateGas()
        val gasLimit = BigInteger.valueOf(300000) // safe limit for first tx to streem-adapter

        try {
            val receipt = Transfer(web3j, transactionManager)
                    .sendFunds(receiver, amount, Convert.Unit.ETHER, gasPrice, gasLimit)
                    .send()

            /*Transfer.sendFunds(
                    web3j, credentials, receiver,
                    BigDecimal.valueOf(0.314), Convert.Unit.ETHER)
                    .send()*/

            println("receipt: " + receipt.toString())
            // TODO: check if receipt.status == "0x01" (success)
        } catch(error: Exception) {
            EventBus.getDefault().post(ErrorEvent("Error: fetch balance: " + error.message))
        }
    }

    // open a streem to the given receiver with the given flowrate
    fun openStreem(context: Context, receiver: String, flowrateCentsPerHour: BigDecimal) {
        try {
            Log.d(TAG, "opening streem to $receiver with flowrate $flowrateCentsPerHour cents/h")
            val receipt = streemContract.openStreem(receiver, toWeiPerSecond(flowrateCentsPerHour)).send()
            Log.d(TAG, "openStreem receipt: $receipt")

            // Since eventbus can communicate with app which is in background or in another process
            // we temporarily trigger notification directly here to avoid issues that the app needs to be on the screen.

            PushNotificationService.buildOngoingPaymentNotification(context)

        } catch (e: java.lang.Exception) {
            val message = "Continuous payment failed to start with error: ${e.message}"
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val title = context.resources.getString(R.string.notification_payment_error_title)
            val builder = PushNotificationService.buildPaymentNotification(context, title, message)
            manager.notify(PushNotificationService.PAYMENT_NOTIFICATION_ID, builder.build())
        }
    }

    // close the last streem which was opened to the given receiver
    fun closeLastStreem(receiver: String) {
        val receipt = streemContract.closeLastStreemTo(receiver).send()
        Log.d(TAG, "closeLastStreem receipt: ${receipt.toString()}")
    }


    fun toWeiPerSecond(centsPerHour: BigDecimal): BigInteger {
        val weiPerHour = Convert.toWei(centsPerHour.divide(BigDecimal.valueOf(100)), Convert.Unit.ETHER)
        if (!Numeric.isIntegerValue(weiPerHour)) {
            throw UnsupportedOperationException(
                    "Non decimal Wei centsPerHour provided: " + centsPerHour + " " + Convert.Unit.ETHER.toString()
                            + " = " + weiPerHour + " Wei")
        }
        val weiPerHourInt = weiPerHour.toBigIntegerExact()

        return weiPerHourInt.divide(BigInteger.valueOf(3600))
    }

    // converts a BigDecimal denoted in ATS/ETH/whatever to a BigInteger in wei (times 10^18)
    fun toBigIntegerInWei(value: BigDecimal): BigInteger {
        // source: Transfer.send()
        val weiValue = Convert.toWei(value, Convert.Unit.ETHER)
        if (!Numeric.isIntegerValue(weiValue)) {
            throw UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " " + Convert.Unit.ETHER.toString()
                            + " = " + weiValue + " Wei")
        }

        return weiValue.toBigIntegerExact()
    }
}