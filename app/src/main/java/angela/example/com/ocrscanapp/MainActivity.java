package angela.example.com.ocrscanapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.microblink.activity.SegmentScanActivity;
import com.microblink.ocr.ScanConfiguration;
import com.microblink.recognizers.blinkocr.parser.generic.AmountParserSettings;
import com.microblink.recognizers.blinkocr.parser.generic.RawParserSettings;

public class MainActivity extends AppCompatActivity {

    private static final int BLINK_OCR_REQUEST_CODE = 100;
    private static final String LICENSE_KEY = "PFZ3YXSA-BDKJT2RV-LGROSDQ4-2LL2Y2ZL-BR4RE5FB-ZJYXXZO5-RJR3P3BX-EH3KTICO";
    private static final String FROM_ACCOUNT = "TotalAmount";
    private static final String TO_ACCOUNT = "Tax";
    private static final String AMOUNT = "IBAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void simpleIntegration(View v) {
        //scans an amount from invoice, tax amount from invoice and IBAN to which amount has to be paid

        Intent intent = new Intent(this, SegmentScanActivity.class);
        intent.putExtra(SegmentScanActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);

        ScanConfiguration conf[] = new ScanConfiguration[] {
                // each scan configuration contains two string resource IDs: string shown in title bar and string shown
                // in text field above scan box. Besides that, it contains name of the result and settings object
                // which defines what will be scanned.
                new ScanConfiguration(R.string.from_title, R.string.from_msg, FROM_ACCOUNT, new RawParserSettings()),
                new ScanConfiguration(R.string.to_title, R.string.to_msg, TO_ACCOUNT, new RawParserSettings()),
                new ScanConfiguration(R.string.amount_title, R.string.amount_msg, AMOUNT, new AmountParserSettings())
        };

        intent.putExtra(SegmentScanActivity.EXTRAS_SCAN_CONFIGURATION, conf);

        // once intent is prepared, we start the BlinkOCRActivity which will preform scan and return results
        // by calling onActivityResult
        startActivityForResult(intent, BLINK_OCR_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // first we need to check that we have indeed returned from BlinkOCRActivity with success
        if(requestCode == BLINK_OCR_REQUEST_CODE && resultCode == SegmentScanActivity.RESULT_OK) {
            // now we can obtain bundle with scan results
            Bundle result = data.getBundleExtra(SegmentScanActivity.EXTRAS_SCAN_RESULTS);

            // each result is stored under key equal to the name of the scan configuration that generated it
            String totalAmount = result.getString(FROM_ACCOUNT);
            String taxAmount = result.getString(TO_ACCOUNT);
            String iban = result.getString(AMOUNT);

            //Toast.makeText(this, "To IBAN: " + iban + " we will pay total " + totalAmount + ", tax: " + taxAmount, Toast.LENGTH_LONG).show();
            TextView t = new TextView(this);
            t = (TextView)findViewById(R.id.textView1);
            t.setText(totalAmount);
            t = (TextView)findViewById(R.id.textView2);
            t.setText(taxAmount);
            t = (TextView)findViewById(R.id.textView3);
            t.setText(iban);
        }
    }
}
