# Aspose.Words Cloud SDK for Android
This repository contains test project and instructions on how to use Aspose.Words Cloud SDK for Java in Android applications.

[Aspose.Words Cloud](https://products.aspose.cloud/words/family "Aspose.Words Cloud")  
[API Reference](https://apireference.aspose.cloud/words/)  

## Key Features
* Conversion between various document-related formats (20+ formats supported), including PDF<->Word conversion
* Mail merge and reports generation 
* Splitting Word documents
* Accessing Word document metadata and statistics
* Find and replace
* Watermarks and protection
* Full read & write access to Document Object Model, including sections, paragraphs, text, images, tables, headers/footers and many others

### Prerequisites

To use Aspose Words Cloud SDK for Java you need to register an account with [Aspose Cloud](https://www.aspose.cloud/) and lookup/create App Key and SID at [Cloud Dashboard](https://dashboard.aspose.cloud/#/apps). There is free quota available. For more details, see [Aspose Cloud Pricing](https://purchase.aspose.cloud/pricing).

## Dependencies
- Java 1.7+
- referenced packages (see [here](pom.xml) for more details)

## Licensing
 
All Aspose.Words Cloud SDKs, helper scripts and templates are licensed under [MIT License](https://github.com/aspose-words-cloud/aspose-words-cloud-java/blob/master/LICENSE). 

## Contact Us
Your feedback is very important to us. Please feel free to contact us using our [Support Forums](https://forum.aspose.cloud/c/words).

## Getting Started
1. **Sign Up**. Before you begin, you need to sign up for an account on our [Dashboard](https://dashboard.aspose.cloud/) and retrieve your [credentials](https://dashboard.aspose.cloud/#/apps).
2. **Minimum requirements**. This SDK requires [Java 1.7 or later](https://java.com/download/).
3. **Add Aspose.Words Cloud SDK for Java to your project**. The project in this repo references [Aspose.Words Cloud SDK for Java](https://github.com/aspose-words-cloud/aspose-words-cloud-java) either by placing the binary inside *app/libs* folder with manual handling of transitive dependenices (meant for internal usage only), or by simple addition of the repository dependency with configuring the project accordingly (user scenario) - the behavior is controlled with *CI* environment variable (see [build.gradle](app/build.gradle)). For the users, the following basic instructions should be applied.

Since this library is consuming Aspose.Words Cloud web APIs, please add *INTERNET* permission to your manifest.
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.your.product.application">
	...
   <uses-permission android:name="android.permission.INTERNET" />
   ...
</manifest>
```
Add [Aspose Cloud repository](https://repository.aspose.cloud).
```gradle
repositories {
    ...
    maven { url 'https://repository.aspose.cloud/repo/' }
    ...
}
```
Add dependency to [Aspose.Words Cloud SDK for Java](https://github.com/aspose-words-cloud/aspose-words-cloud-java), starting from *18.11* (earlier versions may not work with Android).
```gradle
dependencies {
    ...
    implementation group: 'com.aspose', name: 'aspose-words-cloud', version: '19.10'
    ...
}
```
4. **Using the SDK**. The best way to become familiar with how to use the SDK is to read the [Developer Guide](https://docs.aspose.cloud/words/developer-guide/). The [Getting Started Guide](https://docs.aspose.cloud/words/getting-started/) will help you to become familiar with the common concepts.


## Resources
 
[Website](https://www.aspose.cloud/)  
[Product Home](https://products.aspose.cloud/words/family)  
[API Reference](https://apireference.aspose.cloud/words/)  
[Documentation](https://docs.aspose.cloud/words/)  
[Blog](https://blog.aspose.cloud/category/words/)  
 
## Other languages
We generate our SDKs in different languages so you may check if yours is available in our [list](https://github.com/aspose-words-cloud).
 
If you don't find your language in the list, feel free to request it from us, or use raw REST API requests as you can find it [here](https://products.aspose.cloud/words/curl).
