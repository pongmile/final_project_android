final_project_android

# การเล่น

ใช้หลักการเดียวกันกับการเล่นเกม X O แต่แทนด้วยภาพน้ำแข็งและเพนกวินแทน

# ความสามารถต่าง ๆ ของ app

- สามารถเล่น X O ได้
- login ด้วย Facebook เพื่อเล่นเกม
- แสดงผู้ชนะได้
- Invite ด้วย Email เพื่อชวนให้อีกฝั่งเล่นได้
- กดยอมรับเพื่อนที่ Invite มายังฝั่งเราได้
- แสดงหน้าตา + ชื่อของผู้ login
- สามารถแชร์ภาพหน้าจอขณะเล่นหรือเมื่อแข่งจบได้

# การออกแบบหน้าจอ หรือ User Interface ในแต่ละหน้าของ app

# สรุป APIs ที่ใช้งาน ประกอบไปด้วย

## - Facebook Login API

ใช้ในการ Login ด้วย Facebook และดึงข้อมูลผู้ใช้งาน

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
ใช้ในการยืนยันตัวของผู้ใช้งานต่างๆ และเก็บเอาไว้

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
ใช้ในการเก็บข้อมูลการเล่น และการจับคู่ของผู้เล่นต่างๆ

```
FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

```

# Link ของ VDO สำหรับการนำเสนอผลงาน

# Link ของไฟล์ APK
https://1drv.ms/u/s!Aqwul1mA_SVJgfoOHcq5jVId_FLGWg

กำลัง upload ลง store อยู่ครับ
