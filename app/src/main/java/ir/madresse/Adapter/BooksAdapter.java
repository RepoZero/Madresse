package ir.madresse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ir.madresse.App;
import ir.madresse.Model.Book;
import ir.madresse.R;
import ir.madresse.UI.PDFReader;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Name_Book ;
//        public ImageView overflow;
        public NetworkImageView Niv_Book;


        public MyViewHolder(View view) {
            super(view);
            Name_Book= (TextView) view.findViewById(R.id.Name_Book);
            Niv_Book = (NetworkImageView) view.findViewById(R.id.Niv_Book);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public BooksAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Book book = bookList.get(position);
        holder.Name_Book.setText(book.getName());

        holder.Name_Book.setTypeface(App.FONT_iran_sana_light);
        // loading album cover using Glide library
        ImageLoader imageLoader = App.getInstance().getImageLoader();

        // If you are using NetworkImageView
        holder.Niv_Book.setImageUrl(book.getImageUrl(), imageLoader);
        holder.Niv_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(mContext, PDFReader.class);
                intent.putExtra("URL",book.getPdfUrl());
                mContext.startActivity(intent);

            }
        });



//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow,position);
//            }
//        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view, int position) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_book, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
//        popup.show();
//    }
//
//    /**
//     * Click listener for popup menu items
//     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        private int position;
//
//        public MyMenuItemClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.Open:
//                    Book book = bookList.get(position);
//                    Toast.makeText(mContext, "Open"+":::"+book.getName(), Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }



}
