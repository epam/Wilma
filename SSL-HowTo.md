Wilma + Howto Use SSL/TLS
===========

## Intro
Our standard setup is the following: There is a Client that sends a request to the Server, which sends a response back to the Client.
When we use SSL/TLS, then the Server uses encryption to send the response back. But Client will accept the encrypted response only if the Server is entrusted.
How to do it?
- Server has its own keyStore with private-public key pair, let say keyStore.jks
- Client must trust in the response of the Server so Client shall use trustStore.jks with cer file that was exported from the Server and transferred to the Client.

In case we use our proxy in the middle then:
- Client must trust in Proxy -> the trustStore of Client must contain cer of the Proxy keyStore
- Proxy must trust in Server -> the Proxy must accept all certificates (this is used currently by Wilma) or the truststore of Proxy must contain cer of the Server keyStore.

The situation is even more complex if we would like to use 2-Way-SSL, which means not just the Server, but the Client use SSL transfer too.
So in addition for the above setup, we need to have:
- Proxy must trust Client -> the trustStore of Proxy must contain cer of the Client keyStore
- Server must trust Proxy -> the trustStore of Server must contain cer of the Proxy keyStore

Where to find the certificate of Wilma? - in the certificate folder, the password is "vvilma".

## How to manipulate with key/trustStores
Using this tool is recommended: https://keystore-explorer.org/downloads.html 

### Adding a cer file to a trustStore
```
keytool -keystore trustStore.jks -alias keyN -import -file a_cert_file.cer
```
### Additional info about how the BrowserMob-Proxy+LittleProxy works with MITM
https://github.com/lightbody/browsermob-proxy/blob/master/mitm/README.md 