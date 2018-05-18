![logo](https://i.imgur.com/bVbtVPV.png)

# Easy.Api
Easy.Api is a bundle of utility classes which helps to call the RESTful webservices as quickly as never before. It also allows use of RxJava with only fewer implementation detail. It also works with kotlin.

***

It is just a wrapper utility class over retrofit call, which provides you ability to write concise and less lines of code with flexibility of changing loading and error handling behavior as per your need on specific screens of your application

Let's see it in action 💻📲

1. Default request:

````
       new EasyApi<Envelop<List<User>>>(Objects.requireNonNull(getActivity()))
                .setLoaderInterface(this)
                .initCall(ApiFactory.getInstance().fetchUsers("1"))
                .execute((response, isSuccess, successMessage) -> {
                    if (isSuccess) {
                        adapter.setUserList(response.getData());
                    }
                });
````

2. With RxJava:

````
       DisposableObserver<?> d = new EasyApiRx<Envelop<List<User>>>(Objects.requireNonNull(getActivity()))
                .setLoaderInterface(this)
                .initCall(ApiFactory.getInstance().fetchUsersWithRx("1"))
                .execute((response, isSuccess, message) -> {
                    if (isSuccess) {
                        adapter.setUserList(response.getData());
                    }
                });
       disposable.add(d);
````

3. In Kotlin:

````
        EasyApi<Envelop<List<User>>>(Objects.requireNonNull<FragmentActivity>(activity))
                .setLoaderInterface(this)
                .initCall(ApiFactory.instance!!.fetchUsers("1"))
                .execute { response, isSuccess, _ ->
                    if (isSuccess) {
                        adapter?.setUserList(response.data!!)
                    }
                }
````

4. In Kotlin with Rx:

````
        val d = EasyApiRx<Envelop<List<User>>>(Objects.requireNonNull<FragmentActivity>(activity))
                .setLoaderInterface(this)
                .initCall(ApiFactory.instance!!.fetchUsersWithRx("1"))
                .execute { response, isSuccess, _ ->
                    if (isSuccess) {
                        adapter?.setUserList(response.data!!)
                    }
                }
       disposable!!.add(d!!)
````

### It will handle below actions for you:

I. Network connectivity check

II. Loading behavior

III. Error handling

IV. Success/failure check


![sample_home](https://i.imgur.com/rWhZXsv.png)
![sample_detail](https://i.imgur.com/4iU4ZrI.png)

***

## [@Harry's Lab](https://github.com/HarinTrivedi)
