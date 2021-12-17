package id.zeinsetyobudi.githubuserlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.zeinsetyobudi.githubuserlist.apicontroller.FollowResponseItem
import id.zeinsetyobudi.githubuserlist.databinding.FragmentFollowerBinding

class FollowingFragment : Fragment() {

    private lateinit var actionFollowingBinding: FragmentFollowerBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val person = activity?.intent?.getParcelableExtra<User>(EXTRA_USER) as User

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        detailViewModel.stateFollowing.observe(this, {
            it.getContentIfNotHandled()?.let {
                person.username?.let { it -> detailViewModel.getFollowingList(it) }
            }
        })

        detailViewModel.listFollowing.observe(this, { followingList ->
            setRecycleFollowingList(followingList)
        })

        detailViewModel.isLoadingFollowing.observe(this, {
            showLoading(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        actionFollowingBinding = FragmentFollowerBinding.inflate(layoutInflater, container, false)
        return actionFollowingBinding.root
    }

    private fun setRecycleFollowingList(followers: List<FollowResponseItem>){
        val listFollower = ArrayList<Follower>()
        for (users in followers) {
            val follower = Follower(
                users.login,
                users.avatarUrl
            )
            listFollower.add(follower)
        }

        actionFollowingBinding.rvFollower.layoutManager = LinearLayoutManager(context)
        val listFollowerAdapter = FollowerAdapter(listFollower)
        actionFollowingBinding.rvFollower.adapter = listFollowerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        actionFollowingBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}