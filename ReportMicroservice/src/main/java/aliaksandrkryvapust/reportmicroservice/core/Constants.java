package aliaksandrkryvapust.reportmicroservice.core;

public final class Constants {
    public static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    public static final String ENCODING = "UTF-8";
    public static final String TOKEN_VERIFICATION_URI = "http://localhost:8090/api/v1/validateToken";
    public static final String JOB_IMPORT_URI = "http://localhost:8083//api/v1/journal/food/export";
    public static final String AUDIT_URI = "http://localhost:8081/api/v1/audit";
    public static final String TOKEN_HEADER = "audit_access_token";
    public static final String[] XLSX_COLUMN_TITLES = new String[]{"Record weight", "Record supply date", "Product title", "Product calories",
            "Product proteins", "Product fats", "Product carbohydrates", "Product weight", "Meal title",
            "Ingredient weight", "Ingredient product title", "Ingredient product calories",
            "Ingredient product proteins", "Ingredient product fats", "Ingredient product carbohydrates",
            "Ingredient product weight"};
}