package ir.madresse.UI;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ir.madresse.Adapter.BooksAdapter;
import ir.madresse.Model.Book;
import ir.madresse.R;
import ir.madresse.Tools.SpacesItemDecoration;

import static ir.madresse.App.DB;


public class Books extends AppCompatActivity {






    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private List<Book> bookList;


    ArrayList<String> BookName = new ArrayList<String>();
    ArrayList<String> BookImage = new ArrayList<String>();
    ArrayList<String> BookPdf = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        this.setTitle("کتابخانه");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarbooks);

        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("کتابخانه");




        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        bookList = new ArrayList<>();
        adapter = new BooksAdapter(this, bookList);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(decoration);





        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.library).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    private void prepareAlbums() {


        if(GetBookList()){

            for(int i = 0 ; i<BookName.size();i++){
                Book b = new Book(BookName.get(i),BookImage.get(i),BookPdf.get(i));
                bookList.add(b);
            }

            adapter.notifyDataSetChanged();
        }






    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

            // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }






    private boolean GetBookList(){


        String SelectQuery = "SELECT * FROM Books";
        Cursor cursor = DB.rawQuery(SelectQuery, null);


        if (cursor != null && cursor.getCount() > 0) {

            BookName.clear();
            BookImage.clear();
            BookPdf.clear();


            if (cursor.moveToFirst()) {
                do {
                    String BookNameitem = cursor.getString(cursor.getColumnIndex("BookName"));
                    String BookImageitem = cursor.getString(cursor.getColumnIndex("BookImage"));
                    String BookPdfitem = cursor.getString(cursor.getColumnIndex("BookPdf"));


                    BookName.add(BookNameitem);
                    BookImage.add(BookImageitem);
                    BookPdf.add(BookPdfitem);


                } while (cursor.moveToNext());
            }

            cursor.close();



        }

        return true ;
    }







}





