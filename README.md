# nutrifier-android

## Development

### Running the app on your own phone

Because Android Studio's device connecting can't be trusted, we use Android Debug Bridge (abd). 

1. Enable USB debugging on your phone
2. Enable WiFi debugging
3. Install adb
4. Click 'Connect device with pair code' and check the ip and port provided
5. Pair the device with `adb pair <ip>:<port>` (use ip and port from previous step)
6. Enter the pair code
7. Connect to the device with `adb connect <device-ip>:<port>`
(different ip and port, usually displayed in the wireless debugging screen)
8. Enable reverse connection so you phone connects to you computers backend port with
`adb devices tcp:8080 tcp:8080`
9. You are now connected and Android Studio should detect your device