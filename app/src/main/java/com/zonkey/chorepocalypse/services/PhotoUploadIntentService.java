package com.zonkey.chorepocalypse.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;

import java.util.concurrent.ExecutionException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class PhotoUploadIntentService extends IntentService {

    private static final String ACTION_UPLOAD_PHOTO = "com.zonkey.chorepocalypse.services.action.UPLOAD_PHOTO";
    private static final String EXTRA_CHORE = "com.zonkey.chorepocalypse.services.extra.CHORE";
    private static final String EXTRA_PHOTO_URI = "com.zonkey.chorepocalypse.services.extra.PHOTO_URI";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    public PhotoUploadIntentService() {
        super("PhotoUploadIntentService");
    }

    public static void uploadPhoto(Context context, Chore chore, String photoUri) {
        Intent intent = new Intent(context, PhotoUploadIntentService.class);
        intent.setAction(ACTION_UPLOAD_PHOTO);
        intent.putExtra(EXTRA_CHORE, chore);
        intent.putExtra(EXTRA_PHOTO_URI, photoUri);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_PHOTO.equals(action)) {
                final Chore chore = intent.getParcelableExtra(EXTRA_CHORE);
                final String photoUri = intent.getStringExtra(EXTRA_PHOTO_URI);
                try {
                    uploadPhoto(chore, photoUri);
                } catch (ExecutionException e) {
                    displayErrorNotification();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadPhoto(final Chore chore, String photoUri) throws ExecutionException, InterruptedException {
        final String choreId = chore.getChoreKey();
        StorageReference chorePhotosReference = FirebaseStorage.getInstance().getReference().child("chore_photos").child(choreId).child(Uri.parse(photoUri).getLastPathSegment());
        UploadTask.TaskSnapshot taskSnapshot = Tasks.await(chorePhotosReference.putFile(Uri.parse(photoUri))
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        displayUploadNotification(String.valueOf(choreId.hashCode()), taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                    }
                }));
        if (taskSnapshot.getTask().isSuccessful()) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chores").child(choreId);
            chore.setChorePhotoUrl(taskSnapshot.getDownloadUrl().toString());
            databaseReference.setValue(chore);
            cancelUploadNotification(String.valueOf(choreId.hashCode()));
            displaySuccessNotification(choreId);
        }
    }

    public void displayUploadNotification(String choreId, long bytesTransferred, long bytesTotal) {
        double progress = (100.0 * bytesTransferred / bytesTotal);
        mBuilder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text) + ((int) progress) + "%...")
                .setSmallIcon(R.drawable.ic_cloud_upload_24dp);
        mNotificationManager.notify(choreId.hashCode(), mBuilder.build());
    }

    public void cancelUploadNotification(String choreId) {
        mNotificationManager.cancel(choreId.hashCode());
    }

    private void displaySuccessNotification(String choreId) {
        mBuilder.setContentTitle("Chore uploaded successfully!")
                .setContentText("Now get to work!")
                .setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(choreId.hashCode(), mBuilder.build());
    }

    private void displayErrorNotification() {
        Chore mChore = new Chore();
        mBuilder.setContentTitle("Oops- your chore didn't uploaded!")
                .setContentText("Please try again")
                .setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(mChore.getChoreKey().hashCode(), mBuilder.build());
    }
}
