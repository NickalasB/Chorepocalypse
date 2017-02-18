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
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_PHOTO.equals(action)) {
                final Chore chore = intent.getParcelableExtra(EXTRA_CHORE);
                final String photoUri = intent.getStringExtra(EXTRA_PHOTO_URI);
                try {
                    uploadPhoto(chore, photoUri);
                } catch (ExecutionException e) {
                    // TODO: 2/18/17 display error notification
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
                        displayNotification(choreId, taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                    }
                }));
        if (taskSnapshot.getTask().isSuccessful()) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chores").child(choreId);
            chore.setChorePhotoUrl(taskSnapshot.getDownloadUrl().toString());
            databaseReference.setValue(chore);
            cancelNotification(choreId);
            // TODO: 2/18/17 display a success notification
        }
    }

    public void displayNotification(String choreId, long bytesTransferred, long bytesTotal) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        double progress = (100.0 * bytesTransferred / bytesTotal);
        builder.setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text) + ((int) progress) + "%...")
                .setSmallIcon(R.drawable.ic_cloud_upload_24dp);
        notificationManager.notify(choreId.hashCode(), builder.build());
    }

    public void cancelNotification(String choreId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(choreId.hashCode());
    }
}
