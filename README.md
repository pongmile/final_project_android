final_project_android

# ������

����ѡ������ǡѹ�Ѻ�������� X O ��᷹�����Ҿ��������ྐྵ��Թ᷹

# ��������ö��ҧ � �ͧ app

- ����ö��� X O ��
- login ���� Facebook ���������
- �ʴ���骹���
- Invite ���� Email ���ͪǹ����ա��������
- ������Ѻ���͹��� Invite ���ѧ��������
- �ʴ�˹�ҵ� + ���ͧ͢��� login
- ����ö����Ҿ˹�Ҩ͢���������������觨���

# ����͡Ẻ˹�Ҩ� ���� User Interface �����˹�Ңͧ app

# ��ػ APIs �����ҹ ��Сͺ仴���

## - Facebook Login API

��㹡�� Login ���� Facebook ��д֧�����ż����ҹ

```
private void InitializeFacebook () {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                IncommingRequest();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
```

## Firebase Authentication
��㹡���׹�ѹ��Ǣͧ�����ҹ��ҧ� �����������

```
mAuth = FirebaseAuth.getInstance();
......
currentUser = mAuth.getCurrentUser();
......

private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            currentUser = mAuth.getCurrentUser();
                            getInfo();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
```

## Firebase Database
��㹡���红����š����� ��С�èѺ���ͧ�����蹵�ҧ�

```
FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

```

# Link �ͧ VDO ����Ѻ��ù��ʹͼŧҹ

# Link �ͧ��� APK
https://1drv.ms/u/s!Aqwul1mA_SVJgfoOHcq5jVId_FLGWg

���ѧ upload ŧ store �����Ѻ