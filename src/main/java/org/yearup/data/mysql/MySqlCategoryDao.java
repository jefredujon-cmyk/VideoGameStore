package org.yearup.data.mysql;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("""
                        SELECT 
                            category_id, name, description
                        FROM 
                            categories
                        """);
                ResultSet r = stmt.executeQuery();
        ){
            while(r.next()){
                Category category = new Category();
                category.setCategoryId(r.getInt("Category_id"));
                category.setName(r.getString("Name"));
                category.setDescription(r.getString("Description"));

                categories.add(category);
            }
        }catch(SQLException e){
            System.out.println("Error getting all categories");
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {

        Category category = new Category();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("""
                SELECT 
                    category_id, name, description
                FROM 
                    categories
                WHERE 
                    category_id = ?
                """)){
            stmt.setInt(1,categoryId);

            ResultSet r = stmt.executeQuery();

            if(r.next()){
                category.setCategoryId(r.getInt("Category_id"));
                category.setName(r.getString("Name"));
                category.setDescription(r.getString("Description"));
            }else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }catch(SQLException e){
            System.out.println("Error getting category with id: " + categoryId);
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO Categories(Name, Description) VALUES(?,?)
                """)){
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());

            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Error adding category");
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {

        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("""
                UPDATE
                    categories
                SET
                    Category_ID = COALESCE(?, Category_ID),
                    Name = COALESCE(?, Name),
                    Description = COALESCE(?, Description)
                WHERE
                    Category_ID = ?
                """)){
            if(category.getCategoryId() == null || category.getCategoryId() == 0){
                stmt.setNull(1, Types.INTEGER);
            }else{
                stmt.setInt(1, category.getCategoryId());
            }
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());

            stmt.setInt(4, categoryId);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("Error updating category");
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("""
                DELETE FROM 
                    Categories
                WHERE 
                    Category_id = ?
                """)){
            stmt.setInt(1, categoryId);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("Error removing category" + e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
