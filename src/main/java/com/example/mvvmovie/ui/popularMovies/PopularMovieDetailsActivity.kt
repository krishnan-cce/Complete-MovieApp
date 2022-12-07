package com.example.mvvmovie.ui.popularMovies


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmovie.databinding.DetailLayoutBinding
import com.example.mvvmovie.model.casts.Cast
import com.example.mvvmovie.model.recomendations.Recomendations
import com.example.mvvmovie.model.similar.Result
import com.example.mvvmovie.model.videos.Videos
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.network.RetrofitClient
import com.example.mvvmovie.ui.MainActivity
import com.example.mvvmovie.ui.MovieViewModel
import com.example.mvvmovie.ui.MovieViewModelFactory
import com.example.mvvmovie.ui.movieCast.CastAdapter
import com.example.mvvmovie.ui.movieCast.CastViewModel
import com.example.mvvmovie.ui.movieCast.CastViewModelFactory
import com.example.mvvmovie.ui.movieCast.OnCastClickListener
import com.example.mvvmovie.ui.movieGenere.GenreAdapter
import com.example.mvvmovie.ui.movieRecomended.OnRecClickListener
import com.example.mvvmovie.ui.movieRecomended.RecomendedAdapter
import com.example.mvvmovie.ui.movieRecomended.RecomendedViewModel
import com.example.mvvmovie.ui.movieRecomended.RecomendedViewModelFactory
import com.example.mvvmovie.ui.movieVideos.OnVideoClickListener
import com.example.mvvmovie.ui.movieVideos.VideoAdapter
import com.example.mvvmovie.ui.movieVideos.VideoViewModel
import com.example.mvvmovie.ui.movieVideos.VideoViewModelFactory
import com.example.mvvmovie.ui.similarMovies.OnItemClickListener
import com.example.mvvmovie.ui.similarMovies.SimilarAdapter
import com.example.mvvmovie.ui.similarMovies.SimilarViewModel
import com.example.mvvmovie.ui.similarMovies.SimilarViewModelFactory
import com.example.mvvmovie.utils.*
import com.example.mvvmovie.utils.SessionManager.Companion.movieId
import com.example.mvvmovie.utils.SessionManager.Companion.upcomingId
import java.text.NumberFormat
import java.util.*
import kotlin.properties.Delegates

class PopularMovieDetailsActivity : AppCompatActivity()

{
    private lateinit var mviewModel: MovieViewModel
    private lateinit var binding : DetailLayoutBinding
    private lateinit var viewModel: PopularMovieViewModel
    private lateinit var castViewModel: CastViewModel
    private lateinit var castAdapter : CastAdapter
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter : VideoAdapter
    private lateinit var recViewModel: RecomendedViewModel
    private lateinit var recAdapter : RecomendedAdapter
    private lateinit var similarMovieViewModel: SimilarViewModel
    private lateinit var similarAdapter : SimilarAdapter
    private lateinit var genreAdapter : GenreAdapter
    //private var profileId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitClient.apiInterface
        val mainRepository = MovieRepository(retrofitService)

        val mvModelProviderFactory = MovieViewModelFactory(application, mainRepository)
        mviewModel = ViewModelProvider(this, mvModelProviderFactory).get(MovieViewModel::class.java)
        val viewModelProviderFactory = PopularViewModelFactory(application, mainRepository)
        val videoModelProviderFactory = VideoViewModelFactory(application, mainRepository)
        val castViewModelProviderFactory = CastViewModelFactory(application, mainRepository)
        val recViewModelProviderFactory = RecomendedViewModelFactory(application, mainRepository)
        val similarViewModelProviderFactory = SimilarViewModelFactory(application, mainRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PopularMovieViewModel::class.java)
        castViewModel = ViewModelProvider(this, castViewModelProviderFactory).get(CastViewModel::class.java)
        videoViewModel = ViewModelProvider(this, videoModelProviderFactory).get(VideoViewModel::class.java)
        recViewModel = ViewModelProvider(this, recViewModelProviderFactory).get(RecomendedViewModel::class.java)
        similarMovieViewModel = ViewModelProvider(this, similarViewModelProviderFactory).get(SimilarViewModel::class.java)

        val pref = this.getSharedPreferences("PREFS", android.content.Context.MODE_PRIVATE)
        val det = pref.getInt("movieId",0)

//        if (movieId != null){
//            this.profileId = movieId
//        }else{
//            this.profileId = upcomingId
//        }

        viewModel.getAllPopMovies(movieId)
        castViewModel.getAllCasts(movieId)
        videoViewModel.getAllVideos(movieId)
        recViewModel.getAllRec(movieId)
        similarMovieViewModel.getAllSimilarMovies(movieId)
        getDetails()
        setupCasts()
        getCasts()
        setupVideos()
        getVideos()
        setupRecomendations()
        getRecomendations()
        setupSimilar()
        getSimilarMovies()
        setupGenre()
        getGenere()



    }

    fun getDetails(){
        viewModel.movieDetails.observe(this, Observer{ response->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        binding.tvPopularity.text =  "("+it.voteCount.toString() +")"
                        binding.ratingBar.rating = (it.voteAverage).toFloat() / 2
                        binding.tvOriginalTitle.text = it.title
                        binding.tvTitlebar.text = it.title
                        binding.ivPosterPath.setImageUrl("https://image.tmdb.org/t/p/w500" + it.posterPath)
                        binding.ivBackdropPath.setImageUrl("https://image.tmdb.org/t/p/w500" + it.backdropPath)
                        binding.tvOverview.text = it.overview
                        binding.tvRating.text = it.voteAverage.toString()
                        binding.details.tvReleaseDate.text = it.releaseDate
                        binding.details.tvLanguage.text = it.originalLanguage
                        val format = NumberFormat.getCurrencyInstance(Locale.US)
                        val budget: String = format.format(it.budget)
                        val revenue: String = format.format(it.revenue)
                        binding.details.tvBudget.text = budget
                        binding.details.tvRevenue.text = revenue
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        toast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {

                }
            }

        })
    }

    fun getGenere(){
        viewModel.getAllGenre().observe(this,Observer{genreList ->
            genreAdapter.setData(genreList)
        })
    }

    fun getCasts(){
        castViewModel.castDetails.observe(this,Observer{response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        if (it.cast.isEmpty()) { binding.cast.castCard.hide(true) } else castAdapter.setData(it.cast)

                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        toast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }

    fun getVideos(){
        videoViewModel.videoDetails.observe(this,Observer{response->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        if (it.results.isEmpty()) { binding.video.videoCard.hide(true) } else videoAdapter.setData(it.results)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        toast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {

                }else -> response.data?.results?.isEmpty()

            }
        })
    }

    fun getRecomendations(){
        recViewModel.recDetails.observe(this,Observer{response->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        if (it.results.isEmpty()) { binding.recomended.recCard.hide(true) } else recAdapter.setData(it.results)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        toast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }
    fun getSimilarMovies(){
        similarMovieViewModel.similarMovieDetails.observe(this,Observer{response->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        if (it.results.isEmpty()) { binding.similar.similarCard.hide(true) } else similarAdapter.setData(it.results)

                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        toast("An error occured: $message")
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }

/*    Recycler View SetUp */





    fun setupGenre(){
        genreAdapter = GenreAdapter()
        binding.rvGenre.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genreAdapter
        }
    }


    fun setupCasts(){
        castAdapter = CastAdapter(OnCastClickListener{item:Cast,_:Int ->
            sendCastResult(item)
        })
        binding.cast.rvCast.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }
    }
    private fun sendCastResult(item: Cast) {
          toast(item.name)
//        val intent = Intent(this, CastActivity::class.java)
//        val editor = this.getSharedPreferences("CAST_PREFS", Context.MODE_PRIVATE).edit()
//        editor.putInt("castId", casts.id)
//        editor.apply()
//        startActivity(intent)
//        finish()
    }

    fun setupVideos(){
        videoAdapter = VideoAdapter(OnVideoClickListener{item: Videos,_:Int ->
            sendVideoResult(item)
        })
        binding.video.rvVideo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = videoAdapter

        }
    }
    private fun sendVideoResult(item: Videos) {
        val urlStr = "https://www.youtube.com/watch?v=" + item.key
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlStr)
        startActivity(intent)
    }

    fun setupRecomendations(){
        recAdapter = RecomendedAdapter(OnRecClickListener{item: Recomendations,_:Int ->
            sendRecMovieResult(item)
        })
        binding.recomended.rvRec.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recAdapter

        }
    }
    private fun sendRecMovieResult(item: Recomendations) {
        SessionManager.saveRecMovieId(item)
        val intent = Intent(this, PopularMovieDetailsActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setupSimilar(){
        similarAdapter = SimilarAdapter(OnItemClickListener{ item: Result, _:Int ->
            sendSimilarMovieResult(item)
        })
        binding.similar.rvSimilar.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarAdapter

        }
    }
    private fun sendSimilarMovieResult(item: Result) {
        SessionManager.saveSimilarMovieId(item)
        val intent = Intent(this, PopularMovieDetailsActivity::class.java)
        startActivity(intent)
        finish()
    }



}