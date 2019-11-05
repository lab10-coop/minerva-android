# needs env variable SERVER_KEY set
# needs a payload file as parameter

set -e
set -u

curl -X POST --header "Authorization: key=${SERVER_KEY}" --Header "Content-Type: application/json" 	https://fcm.googleapis.com/fcm/send -d @${1}
