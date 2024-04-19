package net.farkhan.oismobile.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import id.co.softorb.lib.passti.PASSTI
import id.co.softorb.lib.passti.STIUtility
import kotlinx.android.synthetic.main.activity_main.*
import net.farkhan.oismobile.view.fragment.CheckInFragment
import net.farkhan.oismobile.view.fragment.CheckOutFragment
import net.farkhan.oismobile.R
import net.farkhan.oismobile.view.fragment.OutManualFragment
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    var checkInFragment: Fragment = CheckInFragment()
    var checkOutFragment: Fragment = CheckOutFragment()
    var outManualFragment: Fragment = OutManualFragment()
    val fm = supportFragmentManager
    var active = checkInFragment
    private lateinit var passti: PASSTI
    private lateinit var sti: STIUtility

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_check_in -> {
                fm.beginTransaction().hide(active).show(checkInFragment).commit();
                active = checkInFragment;
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_check_out -> {
                fm.beginTransaction().hide(active).show(checkOutFragment).commit();
                active = checkOutFragment;
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_check_out_manual -> {
                fm.beginTransaction().hide(active).show(outManualFragment).commit();
                active = outManualFragment;
                //startActivity<BarcodeActivity>()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm.beginTransaction().add(R.id.frame_layout, outManualFragment, "3").hide(outManualFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, checkOutFragment, "2").hide(checkOutFragment).commit();
        fm.beginTransaction().add(R.id.frame_layout, checkInFragment, "1").commit();

        sti = STIUtility(this@MainActivity)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
