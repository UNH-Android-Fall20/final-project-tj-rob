package dev.project.ib2d2.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.project.ib2d2.Models.Backup
import dev.project.ib2d2.R
import dev.project.ib2d2.Views.FileView


/* class to handle fileAdapter
 *
 * @ref: https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 * @ref: https://github.com/chefeleUNH/pizzahub-android-fall20-class/blob/master/app/src/main/java/edu/newhaven/pizzahub/controller/PizzeriaAdapter.kt
 */
class FileAdapter(options: FirestoreRecyclerOptions<Backup>)
    :FirestoreRecyclerAdapter<Backup, FileView>(options){
    private val TAG = javaClass.name

    // create the ViewHolder, inflate rows
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_entry, parent, false)

        return FileView(view)
    }


    override fun onBindViewHolder(holder: FileView, position: Int, model: Backup) {
        // handle if a user clicks the buttons

        // spawn a spinner to fill the area
        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        // Reference to an image file in Cloud Storage
        val storageRef = Firebase.storage.getReferenceFromUrl("gs://final-project-9c2ed.appspot.com/${model.fileName}")

        storageRef.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Any> { uri ->
            val imageURL = uri.toString()
            Log.d(TAG, imageURL)
            Glide
                .with(holder.backupImage)
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(holder.backupImage)
        }).addOnFailureListener(OnFailureListener {
            // Handle any errors
        })
/*
        // use glide to load the image late
        Glide
            .with(holder.backupImage)
            .load(storageRef)
            .placeholder(circularProgressDrawable)
            .into(holder.backupImage)
*/
        holder.backupTitle.text = model.title
        holder.backupDesc.text = model.desc
    }

}
