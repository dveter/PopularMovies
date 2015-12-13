package popularmovies.buddyappz.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import popularmovies.buddyappz.com.popularmovies.data.MovieDbColumns;
import popularmovies.buddyappz.com.popularmovies.data.MoviesContentProvider.Movies;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailsActivityFragment extends Fragment {

    public static String DETAILS = "movie_details";

    private Movie mSelectedMovie;

    private List<Trailer> trailerList = new ArrayList<Trailer>();
    RecyclerView.Adapter trailersListAdapter;
    RecyclerView trailersView;
    CardView trailersCard;

    private List<UserReview> reviewsList = new ArrayList<UserReview>();
    RecyclerView.Adapter reviewsListAdapter;
    RecyclerView reviewsView;
    CardView reviewsCard;
    ProgressBar progressReviews;

    private ShareActionProvider mShareActionProvider;

    private CollapsingToolbarLayout mCollapsingToolbar;

    private boolean isFavourited;

    private MovieDbService movieDbService
            ;
    FloatingActionButton fab;

    public DetailsActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(DETAILS, mSelectedMovie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtilities.MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieDbService = retrofit.create(MovieDbService.class);


        View v = inflater.inflate(R.layout.fragment_details, container, false);

        Intent intent = getActivity().getIntent();
        mSelectedMovie = intent.getParcelableExtra(DETAILS);

        // On Screen rotation
        if (mSelectedMovie == null)
            if (savedInstanceState != null)
                mSelectedMovie = savedInstanceState.getParcelable(DETAILS);

        if (mSelectedMovie != null) {
            updateContent(mSelectedMovie, v);
        }

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_details, menu);

        MenuItem shareAction = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareAction);
    }

    private void initFab(final FloatingActionButton fab) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                        Movies.FAVOURITES,
                        null,   // projection
                        MovieDbColumns.MOVIE_GLOBAL_ID + " = ?", // selection
                        new String[] { Long.toString(mSelectedMovie.getGlobalId()) },   // selectionArgs
                        null    // sort order
                );
                int count = cursor.getCount();
                cursor.close();
                return count;
            }

            @Override
            protected void onPostExecute(Integer rowCount) {
                if (rowCount > 0) {
                    isFavourited = true;
                    fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favourite));
                } else {
                    isFavourited = false;
                    fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unfavourite));
                }
            }
        }.execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavourited) {
                    removeFromFavourites();
                } else {
                    addToFavourites();
                }
               /* ContentValues cv = new ContentValues();
                cv.put(MovieDbColumns.TITLE, mSelectedMovie.getTitle());

                Uri result = getActivity().getApplicationContext().getContentResolver().insert(Movies.FAVOURITES, cv);

                mSelectedMovie.setLocalId(Long.parseLong(result.getLastPathSegment()));*/

            }
        });
    }

    private void initTrailersCardview(View v) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);

        trailersView = (RecyclerView) v.findViewById(R.id.recycler_trailers);
        trailersCard = (CardView) v.findViewById(R.id.card_trailers);

        trailersView.setLayoutManager(layoutManager);

        trailersListAdapter = new TrailersListAdapter(getActivity(), trailerList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailerList.get(position).getKey()));
                startActivity(intent);
            }
        });

        trailersView.setAdapter(trailersListAdapter);

        fetchTrailersAsync();
    }

    private void fetchTrailersAsync() {
        Call<VideosResponse> call = movieDbService.loadTrailers(mSelectedMovie.getGlobalId());

        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Response<VideosResponse> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    trailerList.clear();
                    trailerList.addAll(response.body().getTrailerResults());
                    trailersListAdapter.notifyDataSetChanged();

                    if (trailerList.isEmpty()) {
                        trailersCard.setVisibility(View.GONE);
                    } else {
                        trailersCard.setVisibility(View.VISIBLE);
                        initShareProvider();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.error_trailers_fetch, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initShareProvider() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (trailerList.size() > 0)
            intent.putExtra(Intent.EXTRA_TEXT, "Wow, check this out: " + mSelectedMovie.getTitle() + ", " +
                "http://www.youtube.com/watch?v=" + trailerList.get(0).getKey());
        else
            intent.putExtra(Intent.EXTRA_TEXT, "Wow, ask google about this cool movie: " + mSelectedMovie.getTitle());

        // if (mTrailer != null) {
        mShareActionProvider.setShareIntent(intent);
    }

    private void initReviewsCardView(View v) {

        progressReviews = (ProgressBar)v.findViewById(R.id.progressReviews);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);

        reviewsView = (RecyclerView) v.findViewById(R.id.recycler_reviews);
        reviewsCard = (CardView) v.findViewById(R.id.card_reviews);

        reviewsView.setLayoutManager(layoutManager);

        reviewsListAdapter = new ReviewsListAdapter(getActivity(), reviewsList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        reviewsView.setAdapter(reviewsListAdapter);
        fetchReviewsAsync();
    }


    private void fetchReviewsAsync() {
        Call<UserReviewResponse> call = movieDbService.loadMovieReviews(mSelectedMovie.getGlobalId());

        call.enqueue(new Callback<UserReviewResponse>() {
            @Override
            public void onResponse(Response<UserReviewResponse> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    reviewsList.clear();
                    reviewsList.addAll(response.body().getUserReviews());
                    reviewsListAdapter.notifyDataSetChanged();
                    progressReviews.setVisibility(View.GONE);

                    if (reviewsList.isEmpty()) {
                        reviewsCard.setVisibility(View.GONE);
                    } else {
                        reviewsCard.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),
                        R.string.error_reviews_fetch, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void addToFavourites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                ContentValues cv = new ContentValues();

                cv.put(MovieDbColumns.MOVIE_GLOBAL_ID, mSelectedMovie.getGlobalId());
                cv.put(MovieDbColumns.TITLE, mSelectedMovie.getTitle());
                cv.put(MovieDbColumns.POSTER_URL, mSelectedMovie.getPosterUrl());
                cv.put(MovieDbColumns.RATING, mSelectedMovie.getRating());
                cv.put(MovieDbColumns.BIG_IMAGE_URL, mSelectedMovie.getBigImageUrl());
                cv.put(MovieDbColumns.OVERVIEW, mSelectedMovie.getOverView());
                cv.put(MovieDbColumns.DATE, mSelectedMovie.getDate());

                Uri result = getActivity().getApplicationContext().getContentResolver().insert(Movies.FAVOURITES, cv);

                mSelectedMovie.setLocalId(Long.parseLong(result.getLastPathSegment()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                isFavourited = true;
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favourite));
            }
        }.execute();
    }

    private void removeFromFavourites() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues cv = new ContentValues();
                cv.put(MovieDbColumns.MOVIE_GLOBAL_ID, mSelectedMovie.getGlobalId());
                getActivity().getContentResolver().delete(
                        Movies.FAVOURITES,
                        MovieDbColumns.MOVIE_GLOBAL_ID + " = ?",
                        new String[]{Long.toString(mSelectedMovie.getGlobalId())});

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                isFavourited = false;
                fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unfavourite));
            }
        }.execute();
    }


    public void updateContent(Movie movie, View v) {

        this.mSelectedMovie = movie;

        mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsingToolbarLayout);
        mCollapsingToolbar.setTitle(mSelectedMovie.getTitle());

        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        if (getActivity() instanceof DetailsActivity) {
            ((DetailsActivity) getActivity()).setSupportActionBar(toolbar);
            ((DetailsActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);

        fab = (FloatingActionButton) v.findViewById(R.id.favouriteFab);
        // Prepare floating button in actionbar
        initFab(fab);

        ImageView toolbarImage = (ImageView) v.findViewById(R.id.toolbar_image);
        Glide.with(getActivity().getApplicationContext()).load(CommonUtilities
                .MOVIE_DB_BIG_IMAGE_BASE_URL + mSelectedMovie.getBigImageUrl()).centerCrop().into(toolbarImage);

        ImageView poster = (ImageView) v.findViewById(R.id.imgDetailsPoster);
        Glide.with(getActivity().getApplicationContext()).load(CommonUtilities
                .MOVIE_DB_IMAGE_BASE_URL + mSelectedMovie.getPosterUrl()).centerCrop().into(poster);

        TextView textRating = (TextView) v.findViewById(R.id.txt_details_rating);
        textRating.setText(String.valueOf(mSelectedMovie.getRating()));

        TextView textOrigTitle = (TextView) v.findViewById(R.id.txt_details_title);
        textOrigTitle.setText(mSelectedMovie.getTitle());

        TextView textReleaseDate = (TextView) v.findViewById(R.id.txt_details_release_date);
        if (mSelectedMovie.getDate().equals("null")) {
            textReleaseDate.setText(R.string.not_available);
        } else {
            textReleaseDate.setText(mSelectedMovie.getDate());
        }

        TextView textOverview = (TextView) v.findViewById(R.id.txt_details_overview);
        if (mSelectedMovie.getOverView().equals("null")) {
            textOverview.setText(R.string.not_available);
        } else {
            textOverview.setText(mSelectedMovie.getOverView());
        }

        initTrailersCardview(v);
        initReviewsCardView(v);
    }
}
