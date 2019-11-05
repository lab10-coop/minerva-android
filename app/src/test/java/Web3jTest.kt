import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.web3j.crypto.WalletUtils

@RunWith(RobolectricTestRunner::class)
class Web3jTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

    // Example test unit for web3j
    @Test
    fun web3j_createWallet() {

        // translates to address 0x29258491830579326f574fe1353169c533af2b55 (mnemonic + password)
        val password = ""
        val mnemonic = "body east convince derive return aisle exhaust idle consider interest oyster effort"
        val credentials = WalletUtils.loadBip39Credentials(password, mnemonic)

        Assert.assertEquals(credentials.address, "0x29258491830579326f574fe1353169c533af2b55")
    }
}
