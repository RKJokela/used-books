package com.rjokela.usedbooks;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class BookListFragment extends Fragment {

    public static final String TAG = "BookListFragment";

    private final int NUM_ENTRIES = 5;

    // holds the info for each book
    private String[] titles;
    private String[] authors;
    private String[] synopses;
    private double[] prices;
    private int[] bookCoverDrawables;

    // stores favorites
    private boolean[] favorited;

    // views
    private TextView[] tvTitles;
    private ImageView[] ivCovers;
    private ImageView[] ivFavorites;

    // view id's
    private int[] titleIds;
    private int[] coverIds;
    private int[] favoriteIds;
    private int[] rowIds;

    public BookListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadBookInfo();

        initViews();
    }

    private void loadBookInfo() {
        titles = getResources().getStringArray(R.array.titles);
        Log.d(TAG, "Loaded " + titles.length + " titles");

        authors = getResources().getStringArray(R.array.authors);
        Log.d(TAG, "Loaded " + authors.length + " authors");

        synopses = getResources().getStringArray(R.array.synopses);
        Log.d(TAG, "Loaded " + synopses.length + " synopses");

        for (boolean b : favorited = new boolean[5]) {
            b = false;
        }

        bookCoverDrawables = new int[5];
        bookCoverDrawables[0] = R.drawable.cover;
        bookCoverDrawables[1] = R.drawable.cover;
        bookCoverDrawables[2] = R.drawable.cover;
        bookCoverDrawables[3] = R.drawable.cover;
        bookCoverDrawables[4] = R.drawable.cover;

        titleIds = new int[5];
        titleIds[0] = R.id.bookList_row1_title;
        titleIds[1] = R.id.bookList_row2_title;
        titleIds[2] = R.id.bookList_row3_title;
        titleIds[3] = R.id.bookList_row4_title;
        titleIds[4] = R.id.bookList_row5_title;

        coverIds = new int[5];
        coverIds[0] = R.id.bookList_row1_image;
        coverIds[1] = R.id.bookList_row2_image;
        coverIds[2] = R.id.bookList_row3_image;
        coverIds[3] = R.id.bookList_row4_image;
        coverIds[4] = R.id.bookList_row5_image;

        favoriteIds = new int[5];
        favoriteIds[0] = R.id.bookList_row1_favorite;
        favoriteIds[1] = R.id.bookList_row2_favorite;
        favoriteIds[2] = R.id.bookList_row3_favorite;
        favoriteIds[3] = R.id.bookList_row4_favorite;
        favoriteIds[4] = R.id.bookList_row5_favorite;

        rowIds = new int[5];
        rowIds[0] = R.id.bookList_row1;
        rowIds[1] = R.id.bookList_row2;
        rowIds[2] = R.id.bookList_row3;
        rowIds[3] = R.id.bookList_row4;
        rowIds[4] = R.id.bookList_row5;

        prices = new double[NUM_ENTRIES];
        int[] pricesInt = getResources().getIntArray(R.array.prices);
        for (int i = 0; i < NUM_ENTRIES; i++) {
            // prices are stored in cents so they can be an int-array
            prices[i] = (double) pricesInt[i] / 100.0;
            Log.d(TAG, "Book " + (i+1) + " price: $" + prices[i]);
        }
    }

    private void initViews() {
        // initialize all the arrays
        tvTitles = new TextView[NUM_ENTRIES];
        ivCovers = new ImageView[NUM_ENTRIES];
        ivFavorites = new ImageView[NUM_ENTRIES];

        // loop once for each book
        for (int i = 0; i < NUM_ENTRIES; i++) {
            // set the title view
            tvTitles[i] = (TextView) getActivity().findViewById(titleIds[i]);
            tvTitles[i].setText(titles[i]);

            // set the cover image
            ivCovers[i] = (ImageView) getActivity().findViewById(coverIds[i]);
            ivCovers[i].setImageResource(bookCoverDrawables[i]);

            // hide the favorite icons initially
            ivFavorites[i] = (ImageView) getActivity().findViewById(favoriteIds[i]);
            ivFavorites[i].setVisibility(View.INVISIBLE);

            // set the click listener for the row
            View row = getActivity().findViewById(rowIds[i]);
            final int finalI = i;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetail(finalI);
                }
            });
        }
    }

    private void showDetail(int index) {
        Log.d(TAG, "Book #" + index + " was clicked");

        Intent intent = new Intent(getActivity(), BookDetail.class);

        // send all the book data to BookDetail activity
        intent.putExtra(BookDetailFragment.TITLE, titles[index]);
        intent.putExtra(BookDetailFragment.AUTHOR, authors[index]);
        intent.putExtra(BookDetailFragment.PRICE, prices[index]);
        intent.putExtra(BookDetailFragment.SYNOPSIS, synopses[index]);
        intent.putExtra(BookDetailFragment.FAVORITE, favorited[index]);
        intent.putExtra(BookDetailFragment.IMAGE, bookCoverDrawables[index]);

        try {
            startActivityForResult(intent, index);

        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "ACTIVITY NOT FOUND: " + e.getMessage());
            Log.e(TAG, "CLOSING NOW.");
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int index, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Book detail #" + index + " has returned");

            // get whether user checked or unchecked the favorite box
            boolean isFavorite = data.getBooleanExtra(BookDetailFragment.FAVORITE, false);
            if (isFavorite != favorited[index]) {
                favorited[index] = isFavorite;
                String logAction;
                if (isFavorite) {
                    // user "favorited" the book
                    logAction = " added ";
                    ivFavorites[index].setVisibility(View.VISIBLE);
                    showToast(index, true);
                } else {
                    // user "un-favorited" the book
                    logAction = " removed ";
                    ivFavorites[index].setVisibility(View.INVISIBLE);
                    showToast(index, false);
                }
                Log.d(TAG, "Favorite was" + logAction + "on book #" + index);
            }
        } else {
            Log.d(TAG, "ACTIVITY RESULT NOT OK!");
        }
    }

    private void showToast(int index, boolean b) {
        // TODO
    }
}
