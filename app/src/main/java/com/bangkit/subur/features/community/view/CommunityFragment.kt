import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.subur.R
import com.bangkit.subur.features.community.data.model.Topic
import com.bangkit.subur.features.community.viewmodel.CommunityViewModel
import com.bangkit.subur.features.community.viewmodel.CommunityViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class CommunityFragment : Fragment(R.layout.fragment_community) {

    private lateinit var postTopicEditText: EditText
    private lateinit var postTopicButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var communityViewModel: CommunityViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val topics = mutableListOf<Topic>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postTopicEditText = view.findViewById(R.id.postTopicEditText)
        postTopicButton = view.findViewById(R.id.postTopicButton)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CommunityAdapter(topics)
        recyclerView.adapter = adapter

        val factory = CommunityViewModelFactory(CommunityRepository(CommunityApiService.create()))
        communityViewModel = ViewModelProvider(this, factory).get(CommunityViewModel::class.java)

        postTopicButton.setOnClickListener {
            val topic = postTopicEditText.text.toString()
            if (topic.isNotEmpty()) {
                val user = firebaseAuth.currentUser
                val token = user?.getIdToken(true)?.result?.token ?: ""
                user?.let {
                    communityViewModel.postTopic(it.uid, topic, token)
                }
            }
        }

        communityViewModel.topicResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val topicResponse = response.body()
                topicResponse?.data?.id?.let { Toast.makeText(context, "Topic posted", Toast.LENGTH_SHORT).show() }
            } else {
                Toast.makeText(context, "Failed to post topic", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
