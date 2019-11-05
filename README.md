[![pipeline status](https://code.lab10.io/graz/10-Minerva/minerva/badges/master/pipeline.svg)](https://code.lab10.io/graz/10-Minerva/minerva/commits/master)


# Minerva


Digital Wallet for decentralized data economy.

With our Minerva App, we are creating a tool for users to manage their own data in a way that empowers them while maintaining a high level of convenience and ease of use. While a physical wallet lets you store money, identification and other types of valuable information, Minerva aims to be ‘your digital wallet’ in every sense of the word. It will be able to store multiple digital identities in a self-sovereign way and give users an easy way to collectively manage logins and the information they share with sites and services they interact with online while keeping existing valuables safe.

Website: https://lab10.coop/en/projects/minerva


## Testing push notification

Minerva implements simple interface to communicate with digital wallet through push notification.

Checkout scripts/push_notification.sh

You need to provide there FCM TOKEN of the device which you want to test and SERVER KEY from firebase console (Project -> Project Settings -> Cloud Messanging -> Server key).

### Notification payload

#### Payment 

	 "title": string
     "body": string
	 "longBody": string
     "action": "payment"
     "amount": integer (currently cents)
	 "currencySymbol": string
	 "timeUnit": character: values: s - seconds, m - minutes, h - hours, d - dasy, w - weeks
	 "receiverWalletAddress": Ethereum wallet address
	 "serviceProviderName": string

## License ##

See [LICENSE](LICENSE)
