package com.looforyou.looforyou.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Token;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.activities.AddABathroomActivity;
import com.looforyou.looforyou.activities.BookmarkActivity;
import com.looforyou.looforyou.activities.ProfileActivity;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.TokenDeserializer;
import com.looforyou.looforyou.utilities.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.Constants.LOGIN;
import static com.looforyou.looforyou.Constants.ONE_YEAR;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText username = (EditText) view.findViewById(R.id.fragment_login_name);
        final TextInputEditText password = (TextInputEditText) view.findViewById(R.id.fragment_login_password);
        Button loginBtn = (Button) view.findViewById(R.id.fragment_login_btn);
        Button signupBtn = (Button) view.findViewById(R.id.fragment_signup_btn);
        final TextView backToLogin = (TextView) view.findViewById(R.id.fragment_login_back_to_login);
        backToLogin.setPaintFlags(backToLogin.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        final TextView loginSignUpTitle = (TextView) view.findViewById(R.id.fragment_login_title);
        final CircleImageView uploadImage = (CircleImageView) view.findViewById(R.id.fragment_login_image);
        final EditText firstName = (EditText) view.findViewById(R.id.fragment_login_first_name);
        final EditText lastName = (EditText) view.findViewById(R.id.fragment_login_last_name);
        final EditText email = (EditText) view.findViewById(R.id.fragment_login_email);
        final TextInputLayout confirmPassword = (TextInputLayout) view.findViewById(R.id.fragment_login_confirm_password_layout);
        final Button largeSignUpButton = (Button) view.findViewById(R.id.fragment_signup_btn_large);
        final LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.fragment_login_linear_layout);

        final UserUtil userUtil = new UserUtil(getContext());
        loginBtn.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View view) {
                Map<String, String> credentials = new HashMap<String, String>();
                if(username.getText().toString().toLowerCase().contains("@")){
                    credentials.put("email", username.getText().toString().toLowerCase().trim());
                }else {
                    credentials.put("username", username.getText().toString().toLowerCase().trim());
                }
                credentials.put("password", password.getText().toString());
                credentials.put("ttl", String.valueOf(ONE_YEAR));

                HttpPost post = new HttpPost(credentials);
                String result = null;
                try {
                    result = post.execute(LOGIN).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(result.isEmpty()){
                    Toast.makeText(getContext(),"invalid username or password", Toast.LENGTH_SHORT).show();
                }else {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer());
                    Gson gson = gsonBuilder.create();
                    Token token = gson.fromJson(result, Token.class);

                    userUtil.setUserToken(token.getID());
                    userUtil.setUserID(token.getUserID());
                    try {
                        ((ProfileActivity) getActivity()).onLoggedIn();
                    }catch(Exception e){}
//                    try {
//                        ((AddABathroomActivity) getActivity()).onLoggedIn();
//                    }catch(Exception e){}
                    try {
                        ((BookmarkActivity) getActivity()).onLoggedIn();
                    }catch(Exception e){}

                    InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    //Reset title
                    try {
                        ((ProfileActivity) getActivity()).getSupportActionBar().setTitle("My Profile");
                    }catch(Exception e){}
                    try {
                        ((BookmarkActivity) getActivity()).getSupportActionBar().setTitle("My Bookmarks");
                    }catch(Exception e){}
                    getActivity().onBackPressed();
                    return;
                }
                //hides keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin.setVisibility(View.VISIBLE);
                username.setHint("Username");
                loginSignUpTitle.setText("Sign Up");
                uploadImage.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                confirmPassword.setVisibility(View.VISIBLE);
                largeSignUpButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin.setVisibility(View.GONE);
                username.setHint("Email/Username");
                loginSignUpTitle.setText("Log In");
                uploadImage.setVisibility(View.GONE);
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                confirmPassword.setVisibility(View.GONE);
                largeSignUpButton.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
