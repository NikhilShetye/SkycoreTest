package com.artium.app.helper

import com.artium.app.ArtiumApp
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.*

@Singleton
class UnsafeOkHttpClient @Inject constructor(private val artiumApp: ArtiumApp) {
    /**
     * @param okHttpBuilder
     * @return
     */
    fun getUnsafeOkHttpClient(okHttpBuilder: OkHttpClient.Builder): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            okHttpBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            okHttpBuilder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            okHttpBuilder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

//    private val certificate = "-----BEGIN CERTIFICATE-----\n" +
//            "MIIGdzCCBF+gAwIBAgIQSvlIycsqIB3RbqrSODrCLTANBgkqhkiG9w0BAQwFADBL\n" +
//            "MQswCQYDVQQGEwJBVDEQMA4GA1UEChMHWmVyb1NTTDEqMCgGA1UEAxMhWmVyb1NT\n" +
//            "TCBSU0EgRG9tYWluIFNlY3VyZSBTaXRlIENBMB4XDTIxMDQwOTAwMDAwMFoXDTIx\n" +
//            "MDcwODIzNTk1OVowIDEeMBwGA1UEAxMVZGV2LmFydGl1bWFjYWRlbXkuY29tMIIB\n" +
//            "IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw8esTlGDizpp3ZoQlqzP/xLz\n" +
//            "0F6qC9Km41mFK7mEBrpvGvfVCeur0vIq4FFjrAJ4TgkXU4JF5EQ1UhntBgEHOD0E\n" +
//            "bjsfFA/NtPDthQAidco+NcMeBZppnAO3p6YZ/Jg7BL9CZQ8qIoz8lTYbPZpgFiMj\n" +
//            "24JdGw5TDJvVgy/JzkCvju5MfnZSn0aO2HBneBiRW3MLxvJCs8WdPYmTtAF0AXq0\n" +
//            "EHpqk3YH6kPkK179vcBiOBrmXanV2TB2fQZabmKER1EUjxVu9F1Wl1qhXC+u7qjd\n" +
//            "fhwUVMQHM5nXwWuMc6EXCKrdp5EcKZDqNzSXlZz678YKDJ7a56BshR/aDT3i3QID\n" +
//            "AQABo4ICgDCCAnwwHwYDVR0jBBgwFoAUyNl4aKLZGWjVPXLeXwo+3LWGhqYwHQYD\n" +
//            "VR0OBBYEFA95SjP3heT1qEpmcJTrURq9oS8YMA4GA1UdDwEB/wQEAwIFoDAMBgNV\n" +
//            "HRMBAf8EAjAAMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjBJBgNVHSAE\n" +
//            "QjBAMDQGCysGAQQBsjEBAgJOMCUwIwYIKwYBBQUHAgEWF2h0dHBzOi8vc2VjdGln\n" +
//            "by5jb20vQ1BTMAgGBmeBDAECATCBiAYIKwYBBQUHAQEEfDB6MEsGCCsGAQUFBzAC\n" +
//            "hj9odHRwOi8vemVyb3NzbC5jcnQuc2VjdGlnby5jb20vWmVyb1NTTFJTQURvbWFp\n" +
//            "blNlY3VyZVNpdGVDQS5jcnQwKwYIKwYBBQUHMAGGH2h0dHA6Ly96ZXJvc3NsLm9j\n" +
//            "c3Auc2VjdGlnby5jb20wggEDBgorBgEEAdZ5AgQCBIH0BIHxAO8AdQB9PvL4j/+I\n" +
//            "VWgkwsDKnlKJeSvFDngJfy5ql2iZfiLw1wAAAXi0wPndAAAEAwBGMEQCIEj9Oppr\n" +
//            "JGKuomAYzKjVaMnWgHbOhHcyzRa9F/Kyrt68AiBv5OZbw7gBXOnKj3KkFmqeSZsS\n" +
//            "BqLnl5umE1Pde/73+wB2AJQgvB6O1Y1siHMfgosiLA3R2k1ebE+UPWHbTi9YTaLC\n" +
//            "AAABeLTA+b4AAAQDAEcwRQIgZ5c9YEXtqpIWVvLlno2VFRemo4ZENYlGNSsc4VzH\n" +
//            "MeQCIQDlwoFdBLuXoFORnqIIbxlDP3jaAdWCcylLwcciDEEndTAgBgNVHREEGTAX\n" +
//            "ghVkZXYuYXJ0aXVtYWNhZGVteS5jb20wDQYJKoZIhvcNAQEMBQADggIBAFY9zEpv\n" +
//            "z6QyVeyDSi+2Vh/bPHMR2pgkWhYMuwt2G+nHLeot00kOV8tGs5ueQZrbZ4MpzPuo\n" +
//            "5SU7NLJqz2jYup4xzS/9rY071xhE5Jh1u2+XeLhcJEdm/39Sk27cQmL89II2Fz98\n" +
//            "aUOLEFwcMbYkNTM++FMvrdZWoKYgKiRjYfGTvaSoH8IK9rJNxwVkYkJWoDrK7qlt\n" +
//            "efgDcKEeQ5ZZ3sOAhwcaF5h+lukwngvOlO8Mto686f2H7vdqKYO3FffjWWcqyNYI\n" +
//            "Tqlu4DfSd9ixwOHBUxFl6vCKRCX2zaW//KTKEZKTclSAwz4nGr6ajQn4RU+hrLKJ\n" +
//            "IxOJobBrWWBTI5WmuUPZNvL0Rfnk4IcvS3dIQYlEp8ASnUpFVBGi/FIeFOhv/81j\n" +
//            "au/6cjvCyVw0HDHPfsQ/Pldmm0mvguIxYVO1u22UZf9IvM4fyheSXOfyNKUUx+f9\n" +
//            "u/MMuhyoUtl8wxlzlXqJBIz1ZIhqycNSGkwm9o0ahwUmyJvmpqp1edwXg8cO35+g\n" +
//            "nkHjVsTxrFRlFFxzB4vfpsD4juAbjHevJ93ynt8Bwb9isWto5G+/v+b6qib5aJx5\n" +
//            "xq590qETfSgwgV4bSmjWJ/D4d9C4hUNihjD9IJNdrIsSbKvgBWEppBpLPYMkg3Y9\n" +
//            "aB81/JAcHQYTEhhEl85N4zQaHkUy2yoAYKok\n" +
//            "-----END CERTIFICATE-----"
//
//    private val ca_bundle = "-----BEGIN CERTIFICATE-----\n" +
//            "MIIG1TCCBL2gAwIBAgIQbFWr29AHksedBwzYEZ7WvzANBgkqhkiG9w0BAQwFADCB\n" +
//            "iDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCk5ldyBKZXJzZXkxFDASBgNVBAcTC0pl\n" +
//            "cnNleSBDaXR5MR4wHAYDVQQKExVUaGUgVVNFUlRSVVNUIE5ldHdvcmsxLjAsBgNV\n" +
//            "BAMTJVVTRVJUcnVzdCBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMjAw\n" +
//            "MTMwMDAwMDAwWhcNMzAwMTI5MjM1OTU5WjBLMQswCQYDVQQGEwJBVDEQMA4GA1UE\n" +
//            "ChMHWmVyb1NTTDEqMCgGA1UEAxMhWmVyb1NTTCBSU0EgRG9tYWluIFNlY3VyZSBT\n" +
//            "aXRlIENBMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAhmlzfqO1Mdgj\n" +
//            "4W3dpBPTVBX1AuvcAyG1fl0dUnw/MeueCWzRWTheZ35LVo91kLI3DDVaZKW+TBAs\n" +
//            "JBjEbYmMwcWSTWYCg5334SF0+ctDAsFxsX+rTDh9kSrG/4mp6OShubLaEIUJiZo4\n" +
//            "t873TuSd0Wj5DWt3DtpAG8T35l/v+xrN8ub8PSSoX5Vkgw+jWf4KQtNvUFLDq8mF\n" +
//            "WhUnPL6jHAADXpvs4lTNYwOtx9yQtbpxwSt7QJY1+ICrmRJB6BuKRt/jfDJF9Jsc\n" +
//            "RQVlHIxQdKAJl7oaVnXgDkqtk2qddd3kCDXd74gv813G91z7CjsGyJ93oJIlNS3U\n" +
//            "gFbD6V54JMgZ3rSmotYbz98oZxX7MKbtCm1aJ/q+hTv2YK1yMxrnfcieKmOYBbFD\n" +
//            "hnW5O6RMA703dBK92j6XRN2EttLkQuujZgy+jXRKtaWMIlkNkWJmOiHmErQngHvt\n" +
//            "iNkIcjJumq1ddFX4iaTI40a6zgvIBtxFeDs2RfcaH73er7ctNUUqgQT5rFgJhMmF\n" +
//            "x76rQgB5OZUkodb5k2ex7P+Gu4J86bS15094UuYcV09hVeknmTh5Ex9CBKipLS2W\n" +
//            "2wKBakf+aVYnNCU6S0nASqt2xrZpGC1v7v6DhuepyyJtn3qSV2PoBiU5Sql+aARp\n" +
//            "wUibQMGm44gjyNDqDlVp+ShLQlUH9x8CAwEAAaOCAXUwggFxMB8GA1UdIwQYMBaA\n" +
//            "FFN5v1qqK0rPVIDh2JvAnfKyA2bLMB0GA1UdDgQWBBTI2XhootkZaNU9ct5fCj7c\n" +
//            "tYaGpjAOBgNVHQ8BAf8EBAMCAYYwEgYDVR0TAQH/BAgwBgEB/wIBADAdBgNVHSUE\n" +
//            "FjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwIgYDVR0gBBswGTANBgsrBgEEAbIxAQIC\n" +
//            "TjAIBgZngQwBAgEwUAYDVR0fBEkwRzBFoEOgQYY/aHR0cDovL2NybC51c2VydHJ1\n" +
//            "c3QuY29tL1VTRVJUcnVzdFJTQUNlcnRpZmljYXRpb25BdXRob3JpdHkuY3JsMHYG\n" +
//            "CCsGAQUFBwEBBGowaDA/BggrBgEFBQcwAoYzaHR0cDovL2NydC51c2VydHJ1c3Qu\n" +
//            "Y29tL1VTRVJUcnVzdFJTQUFkZFRydXN0Q0EuY3J0MCUGCCsGAQUFBzABhhlodHRw\n" +
//            "Oi8vb2NzcC51c2VydHJ1c3QuY29tMA0GCSqGSIb3DQEBDAUAA4ICAQAVDwoIzQDV\n" +
//            "ercT0eYqZjBNJ8VNWwVFlQOtZERqn5iWnEVaLZZdzxlbvz2Fx0ExUNuUEgYkIVM4\n" +
//            "YocKkCQ7hO5noicoq/DrEYH5IuNcuW1I8JJZ9DLuB1fYvIHlZ2JG46iNbVKA3ygA\n" +
//            "Ez86RvDQlt2C494qqPVItRjrz9YlJEGT0DrttyApq0YLFDzf+Z1pkMhh7c+7fXeJ\n" +
//            "qmIhfJpduKc8HEQkYQQShen426S3H0JrIAbKcBCiyYFuOhfyvuwVCFDfFvrjADjd\n" +
//            "4jX1uQXd161IyFRbm89s2Oj5oU1wDYz5sx+hoCuh6lSs+/uPuWomIq3y1GDFNafW\n" +
//            "+LsHBU16lQo5Q2yh25laQsKRgyPmMpHJ98edm6y2sHUabASmRHxvGiuwwE25aDU0\n" +
//            "2SAeepyImJ2CzB80YG7WxlynHqNhpE7xfC7PzQlLgmfEHdU+tHFeQazRQnrFkW2W\n" +
//            "kqRGIq7cKRnyypvjPMkjeiV9lRdAM9fSJvsB3svUuu1coIG1xxI1yegoGM4r5QP4\n" +
//            "RGIVvYaiI76C0djoSbQ/dkIUUXQuB8AL5jyH34g3BZaaXyvpmnV4ilppMXVAnAYG\n" +
//            "ON51WhJ6W0xNdNJwzYASZYH+tmCWI+N60Gv2NNMGHwMZ7e9bXgzUCZH5FaBFDGR5\n" +
//            "S9VWqHB73Q+OyIVvIbKYcSc2w/aSuFKGSA==\n" +
//            "-----END CERTIFICATE-----\n"
//
//    /**
//     *
//     * @param okHttpBuilder Builder
//     * @return OkHttpClient.Builder
//     */
//    fun getSecureOkHttpClientBuilder(okHttpBuilder: OkHttpClient.Builder): OkHttpClient.Builder {
//        // This implementation just embeds the PEM files in Java strings; most applications will
//        // instead read this from a resource file that gets bundled with the application.
//
//        val certFactory = CertificateFactory.getInstance("X.509")
//
//        val certificateStream = artiumApp.resources.openRawResource(R.raw.certificate)
//        val caBundleStream = artiumApp.resources.openRawResource(R.raw.ca_bundle)
//
//        val certificate1 = certFactory.generateCertificate(certificateStream) as X509Certificate
//        val certificate2 = certFactory.generateCertificate(caBundleStream) as X509Certificate
//
//        val certificates: HandshakeCertificates = HandshakeCertificates.Builder()
//            .addTrustedCertificate(certificate1)
//            .addTrustedCertificate(certificate2)
//            .build()
//
//        okHttpBuilder.sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
//        return okHttpBuilder
//    }
//
//    /**
//     *
//     * @param okHttpClientBuilder Builder
//     * @return OkHttpClient.Builder
//     */
//    fun setCertificates(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient.Builder {
//        try {
//            val certificateFactory = CertificateFactory.getInstance("X.509")
//            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//            keyStore.load(null, null)
//
//            val ca1: Certificate =
//                certificateFactory.generateCertificate(artiumApp.resources.openRawResource(R.raw.certificate))
//            val ca2: Certificate =
//                certificateFactory.generateCertificate(artiumApp.resources.openRawResource(R.raw.ca_bundle))
//
//            keyStore.setCertificateEntry("ca1", ca1)
//            keyStore.setCertificateEntry("ca2", ca2)
//
//            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//            tmf.init(keyStore)
//            val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init(null, tmf.trustManagers, SecureRandom())
//            okHttpClientBuilder.sslSocketFactory(
//                sslContext.socketFactory,
//                tmf.trustManagers[0] as X509TrustManager
//            )
//            okHttpClientBuilder.hostnameVerifier { _, _ -> true }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return okHttpClientBuilder
//    }
//
//
////    /**
////     *
////     * @param artiumApp ArtiumApp
////     * @param okHttpBuilder Builder
////     * @return OkHttpClient.Builder
////     */
////    fun getSecureOkHttpClientBuilder(
////        artiumApp: ArtiumApp,
////        okHttpBuilder: OkHttpClient.Builder
////    ): OkHttpClient.Builder {
////        // Create a simple builder for our http client, this is only por example purposes
////        /* var httpClientBuilder = OkHttpClient.Builder()
////             .readTimeout(60, TimeUnit.SECONDS)
////             .connectTimeout(60, TimeUnit.SECONDS)*/
////
////        // Here you may wanna add some headers or custom setting for your builder
////
////        // Get the file of our certificate
////        val caFileInputStream = if (artiumApp.artiumSp.isBaseUrlProd()) {
////            artiumApp.resources.openRawResource(R.raw.certificate)
////        } else {
////            artiumApp.resources.openRawResource(R.raw.ca_bundle)
////        }
////
////        // We're going to put our certificates in a Keystore
////        val keyStore = KeyStore.getInstance("PKCS12")
////        keyStore.load(caFileInputStream, BuildConfig.APPLICATION_ID.toCharArray())
////
////        // Create a KeyManagerFactory with our specific algorithm our our public keys
////        // Most of the cases is gonna be "X509"
////        val keyManagerFactory = KeyManagerFactory.getInstance("X509")
////        keyManagerFactory.init(keyStore, BuildConfig.APPLICATION_ID.toCharArray())
////
////        val trustManagerFactory =
////            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
////        trustManagerFactory.init(null as KeyStore?)
////        val trustManagers = trustManagerFactory.trustManagers
////        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
////            "Unexpected default trust managers:" + Arrays.toString(
////                trustManagers
////            )
////        }
////        val trustManager = trustManagers[0] as X509TrustManager
////
////        // Create a SSL context with the key managers of the KeyManagerFactory
////        val sslContext = SSLContext.getInstance("TLS")
////
////        sslContext.init(null, arrayOf(trustManager), SecureRandom())
////
////        //Finally set the sslSocketFactory to our builder and build it
////        okHttpBuilder.sslSocketFactory(sslContext.socketFactory, trustManager)
////        return okHttpBuilder
////    }
}