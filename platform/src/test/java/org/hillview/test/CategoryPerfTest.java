package org.hillview.test;

import org.hillview.sketches.DoubleHistogramBuckets;
import org.hillview.sketches.Histogram;
import org.hillview.sketches.StringHistogramBuckets;
import org.hillview.table.ColumnDescription;
import org.hillview.table.api.ContentsKind;
import org.hillview.table.columns.CategoryListColumn;
import org.hillview.table.columns.DoubleArrayColumn;
import org.hillview.table.columns.StringListColumn;
import org.hillview.table.membership.FullMembershipSet;
import org.hillview.utils.Randomness;
import org.junit.Test;

import java.util.function.Consumer;

public class CategoryPerfTest extends BaseTest {

    private String generateString(int id, int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Randomness random = new Randomness(id);
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt =
                    random.nextInt(25) + 97;
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    private CategoryListColumn generateCategoryListColumn(int colSize, int norm, int sLength, long seed) {
        ColumnDescription desc = new ColumnDescription("col", ContentsKind.Category);
        CategoryListColumn col = new CategoryListColumn(desc);
        Randomness R = new Randomness(seed);
        for (int i = 0; i < colSize; i++)
            col.append( generateString( R.nextInt(norm), sLength));
        return col;
    }

    private StringListColumn generateStringListColumn(int colSize, int norm, int sLength, long seed) {
        ColumnDescription desc = new ColumnDescription("col", ContentsKind.String);
        StringListColumn col = new StringListColumn(desc);
        Randomness R = new Randomness(seed);
        for (int i = 0; i < colSize; i++)
            col.append( generateString( R.nextInt(norm), sLength));
        return col;
    }

    @Test
    public void testPerf() {
        final int colSize = 100000;
        final int norm = 100000;
        final int stringLength = 40;
        final long seed = 0;
        String[] boundaries = new String[26];
        int z = 0;
        for (char i = 'a'; i <=  'z'; i++) {
            String aChar = new Character(i).toString();
            boundaries[z] = aChar;
            z++;
        }
        StringHistogramBuckets buckDes = new StringHistogramBuckets(boundaries);
        Histogram hist = new Histogram(buckDes);
        CategoryListColumn col = generateCategoryListColumn(colSize, norm, stringLength, seed );
        StringListColumn colS = generateStringListColumn(colSize, norm, stringLength, seed);
        FullMembershipSet fMap = new FullMembershipSet(colSize);

        //hist.create(col, fMap, 1.0, 0, false);

        final Consumer<Integer> fCategory = tmp -> { tmp += 1;
                hist.create(col, fMap, 1.0, 0, false);
        };
        TestUtil.runPerfTest("CategoryList", fCategory, 10);

        final Consumer<Integer> fString = tmp -> { tmp += 1;
            hist.create(colS, fMap, 1.0, 0, false);
        };
        TestUtil.runPerfTest("StringList", fString, 10);
        TestUtil.runPerfTest("CategoryList", fCategory, 10);
        TestUtil.runPerfTest("StringList", fString, 10);
    }
}
