package popularmovies.buddyappz.com.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import popularmovies.buddyappz.com.popularmovies.beans.Movie;
import popularmovies.buddyappz.com.popularmovies.data.MovieDbColumns;
import popularmovies.buddyappz.com.popularmovies.data.MoviesContentProvider;

public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private static final String STATE_MOVIE_GRID = "movieGrid";
    private static final String STATE_SORT_TYPE = "sortType";
    private static final String STATE_PAGE_NUMBER = "pageNumber";
    private static final String STATE_NO_CONNECTION = "noConnection";

    AutoFitRecyclerView mRecyclerView;
    RecyclerView.Adapter mMoviesGridAdapter;
    LinearLayout mProgressBar;

    LinearLayout noConnectionView;
    boolean noConnection = false;

    private ArrayList<Movie> movies = new ArrayList<>();

    private AsyncTask<Void, Void, Boolean> getMoviesTask;

    private boolean loading = true;
    private int pageNumber = 1;

    private String currentSortType;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STATE_MOVIE_GRID, movies);
        outState.putString(STATE_SORT_TYPE, currentSortType);
        outState.putInt(STATE_PAGE_NUMBER, pageNumber);
        outState.putBoolean(STATE_NO_CONNECTION, noConnection);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        noConnectionView = (LinearLayout) view.findViewById(R.id.no_connection);

        setHasOptionsMenu(true);

        mRecyclerView = (AutoFitRecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (LinearLayout) view.findViewById(R.id.progress_bar_container);
        mRecyclerView.setHasFixedSize(true);

        noConnectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMoviesInBackground(currentSortType, pageNumber);
            }
        });


        if (savedInstanceState != null) {
            noConnection = savedInstanceState.getBoolean(STATE_NO_CONNECTION);
            movies = savedInstanceState.getParcelableArrayList(STATE_MOVIE_GRID);
            currentSortType = savedInstanceState.getString(STATE_SORT_TYPE);
            pageNumber = savedInstanceState.getInt(STATE_PAGE_NUMBER);

        /*    DetailsActivityFragment detailsFragment = (DetailsActivityFragment) getFragmentManager()
                    .findFragmentById(R.id.fragmentDetails);
            detailsFragment.updateContent(movies.get(selectedIndex), detailsFragment.getView());
*/
        } else {
            currentSortType = CommonUtilities.SORT_BY_POPULAR_KEY;
            getMoviesInBackground(currentSortType, 1);
        }

        mMoviesGridAdapter = new MoviesGridAdapter(getActivity(), movies, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                boolean hasTwoPanes = getResources().getBoolean(R.bool.has_two_panes);

                if (hasTwoPanes) {
                    updateDetailsFragmentByIndex(position);
                }
                else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class)
                            .putExtra(DetailsActivityFragment.DETAILS, movies.get(position));

                    startActivity(intent);
                }
            }
        });

        mRecyclerView.setAdapter(mMoviesGridAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (!currentSortType.equals(CommonUtilities.SORT_BY_FAVORITES)) {

                    int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                    int pastVisiblesItems = ((GridLayoutManager) (mRecyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageNumber++;
                            getMoviesInBackground(currentSortType, pageNumber);
                        }
                    }
                }
            }
        });

        return view;

    }

    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            showPopup();
        }



        return true;
    }

    /*
    Menu popup
     */
    public void showPopup() {
        View menuItemView = getActivity().findViewById(R.id.action_sort);
        PopupMenu popup = new PopupMenu(getActivity(), menuItemView);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sort_popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.most_popular:
                        if ( ! currentSortType.equals(CommonUtilities.SORT_BY_POPULAR_KEY)) {
                            getMoviesInBackground(CommonUtilities.SORT_BY_POPULAR_KEY, 1);
                            Toast.makeText(getActivity(), R.string.toast_sort_by_popular, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.already_sorted_most_popular, Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    case R.id.highest_rated:
                        if (! currentSortType.equals(CommonUtilities.SORT_BY_HIGHEST_RATED_KEY)) {
                            getMoviesInBackground(CommonUtilities.SORT_BY_HIGHEST_RATED_KEY, 1);
                            Toast.makeText(getActivity(), R.string.toast_sort_by_rated, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.already_sorted_hr, Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    case R.id.favorites:
                        if (! currentSortType.equals(CommonUtilities.SORT_BY_FAVORITES)) {
                            getFavoritesInBackground();
                            Toast.makeText(getActivity(), R.string.toast_sort_by_favorites, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.already_sorted_fav, Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    private void getMoviesInBackground(final String sortBy, final int pageNr) {
        getMoviesTask = new AsyncTask<Void, Void, Boolean>() {

            // Temporary list to prevent recyclerView inconstency on slow connections
            List<Movie> tmpMovies;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    JsonResponseParser parser = new JsonResponseParser();

                    tmpMovies = parser.getMoviesListFromJson(ServerUtilities.getPopularMovies(sortBy, pageNr));

                } catch (IOException e) {
                    Log.e(TAG, getString(R.string.err_fetch_movies));
                    e.printStackTrace();
                    return false;
                } catch (JSONException e) {
                    Log.e(TAG, getString(R.string.err_parse_response));
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    noConnection = false;
                    mRecyclerView.setVisibility(View.VISIBLE);
                    noConnectionView.setVisibility(View.GONE);
                    if ( ! sortBy.equals(currentSortType)) {
                        movies.clear();
                        currentSortType = sortBy;
                        pageNumber = 1;
                    }
                    movies.addAll(tmpMovies);
                    mMoviesGridAdapter.notifyDataSetChanged();
                    getMoviesTask = null;
                    mProgressBar.setVisibility(View.GONE);
                    loading = true;

                    if (getResources().getBoolean(R.bool.has_two_panes))
                        updateDetailsFragmentByIndex(0);
                }
                else {
                    getMoviesTask = null;
                    noConnection = true;
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    noConnectionView.setVisibility(View.VISIBLE);
                    currentSortType = sortBy;
                    movies.clear();
                    Toast.makeText(getActivity(), getString(R.string.err_fetch_movies), Toast.LENGTH_SHORT).show();

                }
            }

        };

        getMoviesTask.execute(null, null, null);
    }

    private void getFavoritesInBackground() {
        getMoviesTask = new AsyncTask<Void, Void, Boolean>() {

            // Temporary list to prevent recyclerView inconstency on slow connections
            List<Movie> tmpMovies = new ArrayList<>();

            @Override
            protected Boolean doInBackground(Void... params) {
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                        MoviesContentProvider.Movies.FAVOURITES,
                        new String[]{MovieDbColumns.MOVIE_ID,
                                    MovieDbColumns.MOVIE_GLOBAL_ID,
                                    MovieDbColumns.TITLE,
                                    MovieDbColumns.POSTER_URL,
                                    MovieDbColumns.RATING,
                                    MovieDbColumns.BIG_IMAGE_URL,
                                    MovieDbColumns.OVERVIEW,
                                    MovieDbColumns.DATE},
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Movie movie = new Movie(cursor);
                        tmpMovies.add(movie);
                    } while (cursor.moveToNext());
                    cursor.close();
                }

                return true;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    if (tmpMovies.size() > 0) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        movies.clear();
                        movies.addAll(tmpMovies);

                        noConnectionView.setVisibility(View.GONE);

                        mMoviesGridAdapter.notifyDataSetChanged();
                        getMoviesTask = null;
                        mProgressBar.setVisibility(View.GONE);
                        loading = true;

                        // Select the first one if detailsFragment is already attached (two pane mode)
                        if (getResources().getBoolean(R.bool.has_two_panes)) {
                            updateDetailsFragmentByIndex(0);
                        }

                        currentSortType = CommonUtilities.SORT_BY_FAVORITES;
                    } else {
                        Toast.makeText(MainActivityFragment.this.getActivity(), R.string.no_movies_favorited, Toast.LENGTH_SHORT).show();
                        getMoviesTask = null;
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    getMoviesTask = null;
                    noConnection = true;
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.err_fetch_movies), Toast.LENGTH_SHORT).show();

                }
            }

        };

        getMoviesTask.execute(null, null, null);
    }

    private void updateDetailsFragmentByIndex(int index) {
        // Select the first one if detailsFragment is already attached (two pane mode)
            DetailsActivityFragment detailsFragment = (DetailsActivityFragment) getFragmentManager()
                    .findFragmentById(R.id.fragmentDetails);

            detailsFragment.updateContent(movies.get(index), detailsFragment.getView());

    }

}



