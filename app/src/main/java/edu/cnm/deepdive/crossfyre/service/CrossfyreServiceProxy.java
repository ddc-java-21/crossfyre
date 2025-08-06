package edu.cnm.deepdive.crossfyre.service;

import edu.cnm.deepdive.crossfyre.model.dto.User;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto;
import edu.cnm.deepdive.crossfyre.model.dto.UserPuzzleDto.Guess;
import io.reactivex.rxjava3.core.Single;
import java.time.LocalDate;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Defines the Retrofit API interface for communicating with the Crossfyre backend services.
 * This interface declares HTTP methods for retrieving and updating user profiles and puzzle-related data.
 */
public interface CrossfyreServiceProxy {

  /**
   * Retrieves the authenticated user's profile.
   *
   * @param bearerToken Authorization token in the format {@code "Bearer <token>"}.
   * @return A {@link Single} that emits the authenticated {@link User}.
   */
  @GET("users/me")
  Single<User> getMyProfile(@Header("Authorization") String bearerToken);

  /**
   * Retrieves another user's profile by their unique key.
   *
   * @param bearerToken Authorization token in the format {@code "Bearer <token>"}.
   * @param userKey UUID string identifying the target user.
   * @return A {@link Single} that emits the requested {@link User}.
   */
  @GET("users/{userKey}")
  Single<User> getProfile(@Header("Authorization") String bearerToken, @Path("key") String userKey);

  /**
   * Updates the authenticated user's profile.
   *
   * @param bearerToken Authorization token in the format {@code "Bearer <token>"}.
   * @param profile Updated {@link User} object with desired profile changes.
   * @return A {@link Single} that emits the updated {@link User}.
   */
  @PUT("users/me")
  Single<User> updateProfile(@Header("Authorization") String bearerToken, @Body User profile);

  /**
   * Retrieves the {@link UserPuzzleDto} for the specified date for the authenticated user.
   *
   * @param bearerToken Authorization token in the format {@code "Bearer <token>"}.
   * @param date The target {@link LocalDate} for which to fetch puzzle data.
   * @return A {@link Single} that emits the {@link UserPuzzleDto} for the specified date.
   */
  @GET("userpuzzles/{date}")
  Single<UserPuzzleDto> getUserPuzzle(
      @Header("Authorization") String bearerToken, @Path("date") LocalDate date);

  /**
   * Submits a guess for the puzzle on the specified date.
   *
   * @param bearerToken Authorization token in the format {@code "Bearer <token>"}.
   * @param date The {@link LocalDate} of the puzzle to which the guess applies.
   * @param guess The {@link Guess} object representing the player's submitted guess.
   * @return A {@link Single} that emits the updated {@link UserPuzzleDto} after processing the guess.
   */
  @POST("userpuzzles/{date}/guesses")
  Single<UserPuzzleDto> sendGuess(
      @Header("Authorization") String bearerToken,
      @Path("date") LocalDate date, @Body Guess guess);

}
