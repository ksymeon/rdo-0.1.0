/*
 * Copyright (c) 2013 Kostas Symeonidis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cylog.rdo.test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.cylog.rdo.dao.RdoDao;
import org.cylog.rdo.test.dto.NumberData;
import org.cylog.rdo.test.dto.SimpleInteger;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Full EndToEndTest that can be executed from the command-line or through JUnit.
 *
 * @author Kostas Symeonidis
 */
public class EndToEndMySqlTest {

    // ---- Member variables --------------------------------------------------

    private Connection conn;
    RdoDao dao;

    // ---- Lifecycle ---------------------------------------------------------

    public void setUpInDatabase() throws Exception {
        log("Setting up MySql database connection...");
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/rdo");
        ds.setUser("root");
        ds.setPassword("password");

        this.conn = ds.getConnection();

        // Create the RDO data source now
        this.dao = new RdoDao(ds);
    }

    public void closeConnection() throws Exception {
        log("Closing MySql database connection...");
        if (this.conn != null) {
            this.conn.close();
        }
    }

    // ---- Tests -------------------------------------------------------------

    @Test
    public void dummy() {

    }

    public void doSimpleIntegerTest() throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT integer_value FROM table_of_ints");
        int rowsFound = 0;
        int rowsSum = 0;
        int rowsProduct = 1;
        while (rs.next()) {
            int value = rs.getInt("integer_value");
            rowsFound++;
            rowsSum += value;
            rowsProduct *= value;
        }
        rs.close();
        stmt.close();

        assertThat(rowsFound, is(2));
        assertThat(rowsSum, is(5));
        assertThat(rowsProduct, is(6));

        // This is proper RDO testing

        List<SimpleInteger> list = dao.getDtoList(SimpleInteger.class, "TABLE_OF_INTS", null);
        for (SimpleInteger i : list) {
            log("Found row with value : " + i.getIntegerValue());
        }
    }

    public void doNumbersDataTypeTest() throws Exception {
        List<NumberData> numbers = dao.getDtoList(NumberData.class, "Numbers", null);
        for (NumberData n : numbers) {
            log("Found : " + n);
        }
    }

    // ---- Private methods ---------------------------------------------------

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void log(String format, Object... args) {
        System.out.printf(format + "\n", args);
    }

    // ---- Static main -------------------------------------------------------

    public static void main(String[] args) throws Exception {
        log("Creating end-to-end test for RDO...");
        EndToEndMySqlTest test = new EndToEndMySqlTest();

        test.setUpInDatabase();

        log("Running tests...");
        test.doSimpleIntegerTest();
        test.doNumbersDataTypeTest();

        test.closeConnection();
    }
}
