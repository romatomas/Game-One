package tfcgames.guessthedrink.FaceBookConnector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

public class _FBConnector {
    private final String API_KEY = "1399587140356912";
    private final String[] permissions = {"publish_stream"};

    public Facebook getFacebook() {
        return facebook;
    }

    private Facebook facebook = new Facebook(API_KEY);

    private Context context;
    private Activity activity;

    public _FBConnector(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void authorizeAndPostMassage() {
        facebook.authorize(activity, permissions, new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                Toast.makeText(context, "Authorization successful", Toast.LENGTH_SHORT).show();
                postMessage();
            }

            @Override
            public void onFacebookError(FacebookError e) {
                Toast.makeText(context, "Facebook error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DialogError e) {
                Toast.makeText(context, "Error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Authorization canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postMessage() {
        Bundle messageBunlde = prepareBundle();
        facebook.dialog(context, "feed", messageBunlde, new Facebook.DialogListener() {

            @Override
            public void onComplete(Bundle values) {
                Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFacebookError(FacebookError e) {
                Toast.makeText(context, "Facebook error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DialogError e) {
                Toast.makeText(context, "Error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Authorization canceled", Toast.LENGTH_SHORT).show();
            }});
    }

    private Bundle prepareBundle() {
        Bundle postBundle = new Bundle();
        postBundle.putString("name", "Guess The Drink");
        postBundle.putString("caption", "It's a final countdown!");
        postBundle.putString("description", "New incredible game by TFC Games");
        postBundle.putString("link","http://tfcgames.com");
        postBundle.putString("picture","http://i.gyazo.com/93f87e866500a145288e7471f5508076.png");
        return postBundle;
    }

}
