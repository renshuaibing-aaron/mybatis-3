1.
@Options(useGeneratedKeys = true, keyProperty = "id")
@Insert({"insert into country (countryname,countrycode) values (#{countryname},#{countrycode})"})
int insertBean(Country country);