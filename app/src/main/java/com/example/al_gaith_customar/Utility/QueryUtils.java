/**
 * Created by Osama_Gh on 2018-02-27.
 */
package com.example.al_gaith_customar.Utility;

import android.util.Log;

import com.example.al_gaith_customar.Data.AppData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Sample JSON response for a USGS query
     */

    private static URL url = createUrl(AppData.USGS_REQUEST_URL);
    private static String SAMPLE_JSON_RESPONSE;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing the given JSON response.
     */
//    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
//        // If the JSON string is empty or null, then return early.
//        if (TextUtils.isEmpty(earthquakeJSON)) {
//            return null;
//        }
//
//        // Create an empty ArrayList that we can start adding earthquakes to
//        List<Earthquake> earthquakes = new ArrayList<>();
//
//        // Try to parse the JSON response string. If there's a problem with the way the JSON
//        // is formatted, a JSONException exception object will be thrown.
//        // Catch the exception so the app doesn't crash, and print the error message to the logs.
//        try {
//
//            // Create a JSONObject from the JSON response string
//            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
//
//            // Extract the JSONArray associated with the key called "features",
//            // which represents a list of features (or earthquakes).
//            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
//
//            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
//            for (int i = 0; i < earthquakeArray.length(); i++) {
//
//                // Get a single earthquake at position i within the list of earthquakes
//                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
//
//                // For a given earthquake, extract the JSONObject associated with the
//                // key called "properties", which represents a list of all properties
//                // for that earthquake.
//                JSONObject properties = currentEarthquake.getJSONObject("properties");
//
//                // Extract the value for the key called "mag"
//                double magnitude = properties.getDouble("mag");
//
//                // Extract the value for the key called "place"
//                String[] location;
//                String tempLocation = properties.getString("place");
//                if (tempLocation.contains(" of ")) {
//                    tempLocation.indexOf(" of ");
//                    location = tempLocation.split(" of ");
//                } else {
//                    location = new String[2];
//                    location[0] = "Near the:";
//                    location[1] = tempLocation;
//                }
//
//                // Extract the value for the key called "time"
//                long time = properties.getLong("time");
//
//                // Extract the value for the key called "url"
//                String url = properties.getString("url");
//
//                // Create a new {@link Earthquake} object with the magnitude, location, time,
//                // and url from the JSON response.
//                Earthquake earthquake = new Earthquake(magnitude, location[0], location[1], time, url);
//
//                // Add the new {@link Earthquake} to the list of earthquakes.
//                earthquakes.add(earthquake);
//            }
//
//        } catch (JSONException e) {
//            // If an error is thrown when executing any of the above statements in the "try" block,
//            // catch the exception here, so the app doesn't crash. Print a log message
//            // with the message from the exception.
//            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
//        }
//
//        // Return the list of earthquakes
//        return earthquakes;
//    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            urlConnection.setRequestProperty("Authorization", "Bearer ");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else if (urlConnection.getResponseCode() == 401) {
                inputStream = urlConnection.getErrorStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.println(Log.ASSERT, "Error response code: ", "" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.println(Log.ASSERT, LOG_TAG, "Problem retrieving the  JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String makeHttpRequest(URL url, String auth) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", auth);
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            urlConnection.setRequestProperty("Authorization", "Bearer ");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else if (urlConnection.getResponseCode() == 401) {
                inputStream = urlConnection.getErrorStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.println(Log.ASSERT, "Error response code: ", "" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.println(Log.ASSERT, LOG_TAG, "Problem retrieving the  JSON results.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    public static String fetchData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.println(Log.ASSERT, LOG_TAG, "Problem making the HTTP request." + e);
        }
        Log.println(Log.ASSERT, "data:", jsonResponse);

        return jsonResponse;
    }

    public static String fetchData(String requestUrl, String auth) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, auth);
        } catch (IOException e) {
            Log.println(Log.ASSERT, LOG_TAG, "Problem making the HTTP request." + e);
        }
        return jsonResponse;
    }


}