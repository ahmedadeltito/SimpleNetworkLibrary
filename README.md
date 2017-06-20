# SimpleNetworkLibrary

![alt tag](https://s13.postimg.org/6shufrw87/simple_netwokr_library.png)

Android Simple Network Library for HTTP and Image Requests with cool features implemented with Simple Demo using some Material Design UI Elements.

-----------------------------------------------------------------------------------------------------

# Most Common Library Features:
1. Making HTTP Requests with different Request Types like **GET**, **POST**, **PUT**,**DELETE** and **PATCH**.
2. Making HTTP Requests with different Content Types like **JSON** and **XML**.
3. Caching HTTP Request on **Memory**.
4. Handling Multiple Requests in a **Thread Safe Mechanism**.
5. Making Image Requests **Synchronous** and **Asynchronous** with helpful observers to handle most cases.
6. Caching Image Bitmaps on **Memory**.
7. Creating a **Test Case** for **NetworkRequest** class using **JUnit** testing framework.

# Most Common Simple Demo Application Features:
1. Using some Material Design UI Elements like **CoordinatorLayout**, **CardView**, **AppBarLayout**, **SwipeRefreshLayout**, **RecyclerView** and **Transition**.
2. Handling **Pull To Refresh** and **Load More** mechanism.


-----------------------------------------------------------------------------------------------------

# User Documentation :

1. For simple integration all you have to do is to add the following command in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
and after this, add the Simple Library Network dependency:
```java
	dependencies {
	        compile 'com.github.ahmed-adel-said:SimpleNetworkLibrary:-SNAPSHOT'
	}
```

2. To initialize HTTP request:
```java
networkRequest = NetworkRequest.getInstance(context)
		// base url
                .baseUrl(String baseUrl)
                // endpoint
                .endpoint(String endpoint)
                // request types: GET, POST, PUT, DELETE and PATCH.
                .requestType(RequestType requestType)
                // content types: JSON and XML
                .contentType(ContentType contentType)
                // decode url if the url has spaces like %20%
                .decodedUrl(boolean decodedUrl)
                // add params to your request
                .params(Map<String, String> params)
                // add headers to your request
                .headers(Map<String, String> headers)
                // add json body as a body to your POST, PUT, DELETE and PATCH requests
                .bodyJsonObject(JSONObject bodyJsonObject)
                // OnNetworkRequestResponseListener is a listener that used to observe the success 					and error response
                .onNetworkRequestResponseListener(OnNetworkRequestResponseListener onNetworkRequestResponseListener)
```

3. To fire the request after initializing it:
```java
networkRequest.fireRequest();
```

4. To initialize image HTTP request and load the required image:
```java
userImageView.setImageUrl(
		// image url
		String url,
                // fall back image resource if the image url is not correct and there is an error occurred while downloading the image
                Integer fallbackResource,
                // loading image resource that is appeared during the download of the image 
                Integer loadingResource,
                // OnCompleteImageListener is a listener that used to observe the success downloaded bitmap
                OnCompleteImageListener onCompleteImageListener);
```

5. OnNetworkRequestResponseListener is a observer that used to handle the response of network request calls:
```java
OnNetworkRequestResponseListener() {
                    @Override
                    public void onSuccessResponse(String response, ContentType contentType, boolean isCached) {
                        
                    }

                    @Override
                    public void onErrorResponse(String error, String message, int code) {

                    }
                });
```

6. OnCompleteImageListener is a listener that helps the developer to get the image bitmap if he/she makes an only image single request not a multiple image requests as in the recycler view adapter as an example.:
```java
OnCompleteImageListener() {
                        @Override
                        public void onComplete(Bitmap bitmap) {
                            
                        }
                    })
```
