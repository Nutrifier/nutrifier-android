package fi.nutrifier.services.database

import fi.nutrifier.BuildConfig
import fi.nutrifier.services.AuthService
import fi.nutrifier.services.FineliService
import fi.nutrifier.services.room.RecipeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val baseUrlSpoonacular = "https://api.spoonacular.com/"
    private val baseUrlDb = BuildConfig.BASE_URL
    private val baseUrlFineli = "https://fineli.fi/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofitSpoonacular = Retrofit.Builder()
        .baseUrl(baseUrlSpoonacular)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitDatabase = Retrofit.Builder()
        .baseUrl(baseUrlDb)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "RecipeApp")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofitFineli = Retrofit.Builder()
        .baseUrl(baseUrlFineli)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val recipeService: RecipeService = retrofitSpoonacular.create(RecipeService::class.java)
    val foodEntryService: FoodEntryService = retrofitDatabase.create(FoodEntryService::class.java)
    val foodService: FoodService = retrofitDatabase.create(FoodService::class.java)
    val authService: AuthService = retrofitDatabase.create(AuthService::class.java)
    val fineliService: FineliService = retrofitFineli.create(FineliService::class.java)
    val userService: UserService = retrofitDatabase.create(UserService::class.java)
}