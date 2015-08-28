package tfcgames.guessthedrink.FaceBookConnector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.UiLifecycleHelper;
import com.facebook.internal.Utility;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

public class FBConnector {

    private Context context;
    private Activity activity;

    public FBConnector(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private UiLifecycleHelper fbUIHelper;

    protected void onCreate(Bundle savedInstanceState) {
        fbUIHelper = new UiLifecycleHelper(activity, null);
        fbUIHelper.onCreate(savedInstanceState);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        fbUIHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            //Listener for Facebook-client if installed
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Toast.makeText(context, "Error, try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onResume() {
        fbUIHelper.onResume();
    }

    protected void onSaveInstanceState(Bundle outState) {
        fbUIHelper.onSaveInstanceState(outState);
    }

    protected void onPause() {
        fbUIHelper.onPause();
    }

    protected void onDestroy() {
        fbUIHelper.onDestroy();
    }

    /**
     * Publish link in FaceBook
     *
     * @param name        - title of block
     * @param caption     - text on bottom of block
     * @param description - description of link (between title and caption)
     * @param link        - http:// etc
     * @param pictureLink - http:// etc - link on image in web
     */

    //authorise and post message on facebook
    public final void facebookPublish(String name, String caption, String description, String link, String pictureLink) {
        if (FacebookDialog.canPresentShareDialog(context, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            //Facebook-client is installed
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                    .setName(name)
                    .setCaption(caption)
                    .setDescription(description)
                    .setLink(link)
                    .setPicture(pictureLink)
                    .build();
            fbUIHelper = new UiLifecycleHelper(activity, null);
            fbUIHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            //Facebook-client is not installed – use web-dialog
            Bundle params = new Bundle();
            params.putString("name", name);
            params.putString("caption", caption);
            params.putString("description", description);
            params.putString("link", link);
            params.putString("picture", pictureLink);
            WebDialog feedDialog = new WebDialog.FeedDialogBuilder(context, Utility.getMetadataApplicationId(context), params)
                    .setOnCompleteListener(new WebDialog.OnCompleteListener() {
                        //Listener for web-dialog
                        @Override
                        public void onComplete(Bundle values, FacebookException error) {
                            if ((values != null) && (values.getString("post_id") != null) && (error == null)) {
                                Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Message is not posted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
            feedDialog.show();
        }
    }
}
 