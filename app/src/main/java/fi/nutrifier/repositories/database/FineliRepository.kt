package fi.nutrifier.repositories.database

import android.util.Log
import fi.nutrifier.models.database.FineliResponse
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result

class FineliRepository {
    private val service = RetrofitInstance().fineliService

    suspend fun getFoodsByQuery(query: String): Result<List<FineliResponse>> {
        return try {
            val response = service.getFoods(query)
            Log.d("FineliRepository", response.toString())
            if (response.isSuccessful) {
                Result.Companion.success(response.body() ?: emptyList())
            }
            else Result.Companion.fail(response.code())
        } catch (e: Exception) {
            Result.Companion.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodById(id: Int): Result<FineliResponse> {
        return try {
            val response = service.getFoodById(id)
            if (response.isSuccessful && response.body() != null) Result.Companion.success(response.body())
            else Result.Companion.fail(response.code())
        } catch (e: Exception) {
            Result.Companion.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}