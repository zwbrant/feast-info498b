package edu.uw.info498b.feast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

/**
 * Created by Nick on 5/23/16.
 */
public class DetailFeastActivity extends AppCompatActivity {
    private static final String TAG = "DetailFeastActivity";
    private Bundle bundle;
    private Feast feast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feast);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            int position = bundle.getInt("position");
            feast = MainActivity.feastsAdapter.getItem(position);

            ((TextView)findViewById(R.id.item_title)).setText(feast.name);

//            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
//            ((TextView) findViewById(R.id.item_time)).setText(timeFormat.format(feast.deadline));
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd, yyyy");
//            ((TextView) findViewById(R.id.item_date)).setText(dateFormat.format(feast.date));

            ListView categoryList = (ListView) findViewById(R.id.detail_category_list);


            // people


//             food category
//            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
//                    R.layout.category_list_item, R.id.category_title, feast.categories);
//            ((ListView)findViewById(R.id.detail_category_list)).setAdapter(categoryAdapter);

            if (feast.completed) {
                // result page
                ((ImageView) findViewById(R.id.detail_winning_image)).setVisibility(View.VISIBLE);
//                ((TextView) findViewById(R.id.detail_winning_text)).setText(feast.winning);
                ((TextView) findViewById(R.id.detail_winning_text)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.detail_close_button)).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void handleClosePoll(View v) {
        Toast.makeText(this, "Poll closed!", Toast.LENGTH_SHORT).show();
        feast.completed = true;
        ((ImageView) findViewById(R.id.detail_winning_image)).setVisibility(View.VISIBLE);
//                ((TextView) findViewById(R.id.detail_winning_text)).setText(feast.winning);
        ((TextView) findViewById(R.id.detail_winning_text)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.detail_close_button)).setVisibility(View.INVISIBLE);
    }
}
