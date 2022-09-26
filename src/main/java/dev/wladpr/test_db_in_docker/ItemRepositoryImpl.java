package dev.wladpr.test_db_in_docker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements DbRepository<Item> {
    private Connection connection;
    public ItemRepositoryImpl() {
        try {
            // looking for a class by name and load class in memory.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found " + e);
        }
        // connect to bd.
        String url =  "jdbc:postgresql://localhost:80/postgres?user=user.postgres&password=unknown";
        try {
            // create connect to bd.
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            init();
            save(Item.builder().model("hp").build());
            save(Item.builder().model("Dell").build());
            save(Item.builder().model("apple").build());

            save(Item.builder().color("white").build());
            save(Item.builder().color("black").build());

            save(Item.builder().ram(8).build());
            save(Item.builder().ram(16).build());

            save(Item.builder().hd(1000).build());
            save(Item.builder().hd(2000).build());

            save(Item.builder().price(1000).build());
            save(Item.builder().price(3000).build());

            List<Item> items = fetchAll();
            for (Item item : items) {
                System.out.println(item);
            }
        } catch (SQLException a) {
            throw new RuntimeException(a);
        }


    }



    private void init() throws SQLException {
        // create table in bd.
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS items (
                id SERIAL PRIMARY KEY,
                model TEXT,
                color TEXT,
                ram INTEGER(10),
                hd INTEGER(10),
                price INTEGER(10)
                )
                """;
        try {
            // execute the request.
            connection.createStatement().execute(createTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Item entity) {
        String insertItem = """
                INSERT INTO items (model, color, ram, hd, price)
                VALUES ( ?, ?, ?, ?, ?)
                """;

        try {
            PreparedStatement prepareStatement = connection.prepareStatement(insertItem);
            prepareStatement.setString(1, entity.getModel());
            prepareStatement.setString(1, entity.getColor());
            prepareStatement.setString(1, String.valueOf(entity.getRam()));
            prepareStatement.setString(1, String.valueOf(entity.getHd()));
            prepareStatement.setString(1, String.valueOf(entity.getPrice()));

            prepareStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public List<Item> fetchAll() {
        String fetchItemQuery = """
                SELECT id, model, color, ram, hd, price
                FROM items
                """;

        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery(fetchItemQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Item> items = new ArrayList<>();
        while(true) {
            try {
                if (!resultSet.next()) break;

                Item item = Item.builder()
                        .id(resultSet.getInt("id"))
                        .model(resultSet.getString("model"))
                        .color(resultSet.getString("color"))
                        .hd(resultSet.getInt("ram"))
                        .ram(resultSet.getInt("hd"))
                        .price(resultSet.getInt("price"))
                        .build();
                items.add(item);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return items;
    }

    public static void main(String[] args) {
        new ItemRepositoryImpl();
    }

}


