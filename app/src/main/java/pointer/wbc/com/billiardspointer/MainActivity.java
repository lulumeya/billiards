package pointer.wbc.com.billiardspointer;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import pointer.wbc.com.billiardspointer.fragment.MainActivityFragment;


public class MainActivity extends BaseActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_list) {
//            Navigator.goGameList(context);
//            return true;
//        } else if (id == R.id.action_theme) {
//            boolean isLight = !Pref.getBoolean(Const.KEY_THEME, false);
//            Pref.putBoolean(Const.KEY_THEME, isLight);
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof MainActivityFragment) {
            ((MainActivityFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}