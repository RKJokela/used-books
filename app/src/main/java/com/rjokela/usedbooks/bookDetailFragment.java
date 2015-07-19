package com.rjokela.usedbooks;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;


/**
 * A placeholder fragment containing a simple view.
 */
public class BookDetailFragment extends Fragment {

    public static final String TAG = "BookDetailFragment";

    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String PRICE = "price";
    public static final String SYNOPSIS = "synopsis";
    public static final String FAVORITE = "favorite";
    public static final String IMAGE = "image";

    private boolean favorited;

    private ImageView ivCover;
    private ImageView ivFavorite;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPrice;
    private TextView tvSynopsis;
    private CheckBox cbFavorite;
    private Button   backButton;

    public BookDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent data = getActivity().getIntent();

        // get the info from the Extra bundle
        String title = data.getStringExtra(TITLE);
        String author = data.getStringExtra(AUTHOR);
        double price = data.getDoubleExtra(PRICE, -1.0);
        String synopsis = data.getStringExtra(SYNOPSIS);
        favorited = data.getBooleanExtra(FAVORITE, false);
        int imageId = data.getIntExtra(IMAGE, -1);

        tvTitle = (TextView) getActivity().findViewById(R.id.bookDetail_title);
        tvTitle.setText(title);

        tvAuthor = (TextView) getActivity().findViewById(R.id.bookDetail_author);
        tvAuthor.setText(author);

        tvPrice = (TextView) getActivity().findViewById(R.id.bookDetail_price);
        if (price >= 0.0) {
            DecimalFormat df = new DecimalFormat("$#.00");
            tvPrice.setText(df.format(price));
        }

        tvSynopsis = (TextView) getActivity().findViewById(R.id.bookDetail_synopsis);
        tvSynopsis.setText(synopsis);

        ivCover = (ImageView) getActivity().findViewById(R.id.bookDetail_cover);
        if (imageId >= 0) {
            ivCover.setImageResource(imageId);
        }

        ivFavorite = (ImageView) getActivity().findViewById(R.id.bookDetail_favorite);
        ivFavorite.setVisibility(favorited ?
                                    View.VISIBLE :
                                    View.INVISIBLE);

        cbFavorite = (CheckBox) getActivity().findViewById(R.id.bookDetail_checkBox);
        cbFavorite.setChecked(favorited);
        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favorited = isChecked;
                ivFavorite.setVisibility(favorited ?
                                            View.VISIBLE :
                                            View.INVISIBLE);
            }
        });

        backButton = (Button) getActivity().findViewById(R.id.bookDetail_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(FAVORITE, favorited);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
            }
        });
    }
}
