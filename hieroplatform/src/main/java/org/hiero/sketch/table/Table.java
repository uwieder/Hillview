package org.hiero.sketch.table;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.hiero.sketch.table.api.*;

import java.util.Arrays;

/**
 * This is a simple table held entirely in RAM.
 */
public class Table extends BaseTable {
    @NonNull
    protected final Schema schema;

    @NonNull
    protected final IMembershipSet members;

    /**
     * Create an empty table with the specified schema.
     * @param schema schema of the empty table
     */
    public Table(@NonNull final Schema schema) {
        super(schema);
        this.schema = schema;
        this.members = new FullMembership(0);
    }

    public Table(@NonNull final Iterable<IColumn> columns, @NonNull final IMembershipSet members) {
        super(columns);
        final Schema s = new Schema();
        for (final IColumn c : columns)
            s.append(c.getDescription());
        this.schema = s;
        this.members = members;
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public IRowIterator getRowIterator() {
        return this.members.getIterator();
    }

    /**
     * Describes the set of rows that are really present in the table.
     */
    @Override @NonNull
    public IMembershipSet getMembershipSet() { return this.members; }

    public Table(@NonNull final Iterable<IColumn> columns) {
        this(columns, new FullMembership(columnSize(columns)));
    }

    public String toLongString(int startRow, int rowsToDisplay) {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.toString());
        builder.append(System.getProperty("line.separator"));
        final IRowIterator rowIt = this.members.getIterator();
        int nextRow = rowIt.getNextRow();
        while ((nextRow < startRow) && (nextRow != -1))
            nextRow = rowIt.getNextRow();
        int count = 0;
        while ((nextRow != -1) && (count < rowsToDisplay)) {
            RowSnapshot rs = new RowSnapshot(this, nextRow);
            builder.append(rs.toString());
            builder.append(System.getProperty("line.separator"));
            nextRow = rowIt.getNextRow();
            count++;
        }
        return builder.toString();
    }

    public String toLongString(int rowsToDisplay) {
        return this.toLongString(0, rowsToDisplay);
    }

    @Override
    public int getNumOfRows() {
        return this.members.getSize();
    }

    /**
     * Can be used for testing.
     * @return A small table with some interesting contents.
     */
    public static Table testTable() {
        ColumnDescription c0 = new ColumnDescription("Name", ContentsKind.String, false);
        ColumnDescription c1 = new ColumnDescription("Age", ContentsKind.Int, false);
        StringArrayColumn sac = new StringArrayColumn(c0, new String[] { "Mike", "John", "Tom"});
        IntArrayColumn iac = new IntArrayColumn(c1, new int[] { 20, 30, 10 });

        return new Table(Arrays.asList(sac, iac));
    }

    /**
     * Generates a table that contains all the columns, and only
     * the rows contained in IMembership Set members with consecutive numbering.
     */
    public SmallTable compress() {
        final ISubSchema subSchema = new FullSubSchema();
        return this.compress(subSchema, this.members);
    }
}
