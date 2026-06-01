package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodBarcodeRequest
import fi.nutrifier.models.database.FoodRequest
import fi.nutrifier.models.database.PaginatedResponse
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager
import java.util.UUID

class FoodRepository(private val encryptedPrefs: SharedPreferences) {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.foodService

    suspend fun getFoods(page: Int, size: Int): Result<PaginatedResponse<Food>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getFoods(page, size, "Bearer $token")
            if (response.isSuccessful) {
                val data = response.body()
                Result.success(data)
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getRecentFoods(): Result<List<Food>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getRecentFoods("Bearer $token")
            if (response.isSuccessful) {
                val data = response.body()
                Result.success(data)
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodByIds(ids: List<String>): Result<List<Food>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getFoodByIds(ids, "Bearer $token")
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodsByQuery(page: Int, size: Int, query: String): Result<PaginatedResponse<Food>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getFoodByQuery(page, size, query, "Bearer $token")
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getFoodsByBarcode(query: String): Result<List<Food>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getFoodByBarcode(query, "Bearer $token")
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveFood(foodRequest: FoodRequest): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.saveFood(foodRequest, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateFood(foodId: UUID, foodRequest: FoodRequest): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.updateFood(foodId, foodRequest, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun addBarcode(foodId: String, request: FoodBarcodeRequest): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val foodUUID = UUID.fromString(foodId);

            val response = service.addBarcode(foodUUID, request, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}