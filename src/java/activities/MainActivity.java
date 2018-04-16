package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.theoutdoors.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.CustomCardListAdapter;
import app.VolleyController;
import model.Hikes;

public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    List<Hikes>mHikeArticles;
    Context mContext;
    CustomCardListAdapter mCArdAdapter;
    private ProgressDialog mDialog;
    private final String apiUrl="https://trailapi-trailapi.p.mashape.com/?q[activities_activity_type_name_eq]=hiking&q[country_cont]=United+States";
    private final static String TAG= MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=(Toolbar)findViewById(R.id.main_nav_toolbar);
        mToolbar.setTitle("Outdoors");

//        mDialog=new ProgressDialog(getApplication());
//        mDialog.setMessage("Loading...");
//        mDialog.show();


        mHikeArticles=new ArrayList<>();
        mRecyclerView=findViewById(R.id.main_rv);
        mCArdAdapter=new CustomCardListAdapter(getApplicationContext(),mHikeArticles);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mCArdAdapter);

        fetchRequest();

      


    }


    private void fetchRequest() {
        Log.i(TAG,"in fetch req");

        JsonObjectRequest rootJsonObjReq=new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,""+response);
               // mDialog.hide();
                try {
                    JSONArray placesTab = response.getJSONArray("places");
                    Log.i(TAG,""+placesTab.length());
                    for(int i=0;i<placesTab.length();i++){
                        JSONObject singleObj=placesTab.getJSONObject(i);
                        Hikes hike=new Hikes();
                        hike.setName(singleObj.getString("name"));
                        hike.setState(singleObj.getString("state"));
                        hike.setDescription(singleObj.getString("description"));
                        hike.setLat(singleObj.getDouble("lat"));
                        hike.setLon(singleObj.getDouble("lon"));
                        JSONArray activitiesObj = singleObj.getJSONArray("activities");
                        for(int j=0;j<activitiesObj.length();j++){
                            JSONObject singleActivity=activitiesObj.getJSONObject(i);
                            String thumbNail=singleActivity.getString("thumbnail");
//                            hike.setDescription(singleActivity.getString("description"));
                            hike.setDescription("The Deschutes River State Recreation Area is a tree-shaded, overnight oasis for campers.");
                            if(thumbNail.equals("null"))
                                hike.setThumbnailUrl("http://images.tripleblaze.com/2012/08/IMG_1635-0.jpg");
                            else
                                hike.setThumbnailUrl(thumbNail);
                            if(singleActivity.getString("rank").equals("null"))
                                hike.setRank(0);
                            else
                            hike.setRank(singleActivity.getInt("rank"));
                            hike.getRating(singleActivity.getInt("rating"));
                            hike.getActivity_type_name(singleActivity.getString("activity_type_name"));
                        }


                        mHikeArticles.add(hike);
                    }
                }catch(Exception e){
                    Log.i(TAG,e.toString());
                }
                mCArdAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"ERROR:"+error.getMessage());
//                mDialog.hide();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Mashape-Key", "aoWJ98GIIimsh3Jiv72G310ZcVuup1UFVwujsntjJwqdNbbk26");
                params.put("Accept", "text/plain");

                return params;
            }
            };

        VolleyController.getVolleyInstance(getApplicationContext()).addToRequestQueue(rootJsonObjReq);
    }
}
