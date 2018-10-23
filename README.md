![logo](https://i.imgur.com/ZZMVI7S.png)

# Easy.Api
### Java[ ![Java](https://api.bintray.com/packages/harintrivedi/Easy.Api/EasyApi-Java/images/download.svg) ](https://bintray.com/harintrivedi/Easy.Api/EasyApi-Java/_latestVersion)    Kotlin[ ![Download](https://api.bintray.com/packages/harintrivedi/Easy.Api/EasyApi-Kotlin/images/download.svg) ](https://bintray.com/harintrivedi/Easy.Api/EasyApi-Kotlin/_latestVersion)


## Version 2.0 is here 🎉🌟🤟

**What's new:**
1. Added gradle library dependency instead of standalone classfiles
2. Dedicated dependency for koltin projects
3. More simplified way of usage
4. More is coming, stay tuned

***

Easy.Api is a bundle of utility classes which helps to call the RESTful webservices as quickly as never before. It also allows use of RxJava with only fewer implementation detail. It also works with kotlin.

Reference Blog: [https://android.jlelse.eu/easy-api-forget-boilerplates-implement-web-services-in-a-snap-5995a32f1637](https://android.jlelse.eu/easy-api-forget-boilerplates-implement-web-services-in-a-snap-5995a32f1637)


### It will make your life easy by...

I. Handling Network connectivity check

II. Handling Loading behavior callbacks

III. Handling Error/Exception callback

IV. Return Success/failure checks

V. Easily use with ViewModel & LiveData 😍


***

###  ✍️ It is just a wrapper of utility classes who uses retrofit networking library under the hood, which provides you ability to write concise and less lines of code with flexibility of changing loading and error handling behavior as per your need on specific screens of your application

Let's see it in action 💻📲

## **Integartion:**

````
//1. Java:

   implementation 'com.hlab.easyapi:easyapi-java:2.0.0'

//2. For kotlin lovers:

   implementation 'com.hlab.easyapi:easyapi-kotlin:2.0.0'

````
As kotlin is interoperable with java, any of the dependency will work fine with your code written in either language

## **Implementation:**

You are supposed to implement LoaderInterface for default implementation
  or
you need to add LoadingLiveData in viewmodel to observe loading behavior
You may add this implementation in base class to use it easily. 
[BaseFragment.java](https://github.com/HarinTrivedi/Easy.Api/blob/master/android/src/main/java/com/hlabexmaples/easyapi/ui/base/BaseFragment.java)


1. Default request:

````
       EasyApiCall<Envelop<List<User>>> usersListApi 
           = new EasyApi.Builder<Envelop<List<User>>(Objects.requireNonNull(getActivity()))
             .setLoaderInterface(this)
             .build();

       usersListApi
         .initCall(ApiFactory.getInstance().fetchUsers("1"))
         .execute(true, (response, isError) -> {
           if (!isError)
             adapter.setUserList(response.getData());
         });

````

2. With RxJava:

````
       EasyApiCall<Envelop<List<User>>> usersListApi 
           = new EasyApi.Builder<Envelop<List<User>>(Objects.requireNonNull(getActivity()))
             .setLoaderInterface(this)
             .configureWithRx()
             .build();
      //-----

````

3. In Kotlin:

````
        var usersListApi: EasyApiCall<Envelop<List<User>>>? =
           Builder<Envelop<List<User>>>(activity!!)
           .setLoaderInterface(this)
           .build()

        usersListApi?.initCall(ApiFactory.instance!!.fetchUsers("1"))!!
          .execute(true) { response, isError: Boolean ->
            if (!isError)
              response?.data?.let { adapter?.setUserList(it) }
          }
````

4. In Kotlin with Rx:

````
        var usersListApi: EasyApiCall<Envelop<List<User>>>? =
           Builder<Envelop<List<User>>>(activity!!)
           .setLoaderInterface(this)
           .configureWithRx()
           .build()
        //-----

````

5. Cancel or destroy it:

````
    usersListApi.dispose(); // It will work for both default and Rx-call

````

6. Using view model (v 2.0)

````
public class UserViewModel extends AndroidViewModel {

  private MutableLiveData<List<User>> usersLiveData;
  private MutableLiveData<STATE> loadingStateLiveData;
  private EasyApiCall<Envelop<List<User>>> usersListApi;

  private int pageNo = 1;

  public UserViewModel(Application application) {
    super(application);

    // initialize live data
    usersLiveData = new MutableLiveData<>();
    loadingStateLiveData = new MutableLiveData<>();

    //EasyApi Configuration
    usersListApi = new EasyApi.Builder<Envelop<List<User>>>(Objects.requireNonNull(application))
        .attachLoadingLiveData(loadingStateLiveData).build();
  }

  /**
   * Api call with EasyApi
   * Expose the LiveData Products query so the UI can observe it.
   */
  public void fetchUsers() {
    usersListApi.initCall(ApiFactory.getInstance().fetchUsers(String.valueOf(pageNo)))
        .execute(true, (response, isError) -> {
          if (!isError) {
            usersLiveData.setValue(response.getData());
          }
        });
  }
}
````


![sample_home](https://i.imgur.com/rWhZXsv.png)
![sample_detail](https://i.imgur.com/4iU4ZrI.png)

***

## LICENSE
````
Copyright 2018 Harry's Lab

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
## [@Harry's Lab](https://github.com/HarinTrivedi)
