package edu.cnm.deepdive.crossfyre.service;


import edu.cnm.deepdive.crossfyre.model.dto.GuessDto;
import edu.cnm.deepdive.crossfyre.model.dto.User;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CrossfyreServiceProxy {

  @GET("users/me")
  Single<User> getMyProfile(@Header("Authorization") String bearerToken);

  @GET("users/{userKey}")
  Single<User> getProfile(@Header("Authorization") String bearerToken, @Path("key") String userKey);

  @PUT("users/me")
  Single<User> updateProfile(@Header("Authorization") String bearerToken, @Body User profile);

  @GET("userpuzzles/{date}")
  Single<UserPuzzleDto> getUserPuzzle(
      @Header("Authorization") String bearerToken, @Path("date") Instant date);

  @POST("userpuzzles/{date}/guesses")
  Single<UserPuzzleDto> sendGuess(
      @Header("Authorization") String bearerToken,
      @Path("date") Instant date, @Body GuessDto guess);

}
