package verixos.com.testingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lenovo on 10/26/2017.
 */

/**
 * Created by Belal on 2/26/2017.
 */

public class ArtistList extends ArrayAdapter<Artist> {
    private Activity context;
    List<Artist> artists;

    public ArtistList(Activity context, List<Artist> artists) {
        super(context, R.layout.rows, artists);
        this.context = context;
        this.artists = artists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.rows, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Artist artist = artists.get(position);
        textViewName.setText(artist.getArtistName());
        textViewGenre.setText(artist.getArtistGenre());

        return listViewItem;
    }
}
