@Controller
public class CurrencyConversionController {

    @Autowired
    private RestTemplate restTemplate; // Autowire a RestTemplate bean

    @GetMapping("/currency-converter")
    public String showConverterPage() {
        return "currency-converter";
    }

    @PostMapping("/convert")
    public String convertCurrency(@RequestParam String from, @RequestParam String to, @RequestParam double amount, Model model) {
        // Call the exchange rates API with the provided currencies
        String apiEndpoint = "https://api.exchangeratesapi.io/latest?base=" + from;
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiEndpoint, HttpMethod.GET, null, String.class);

        // Parse the response to get the exchange rates
        JSONObject responseJson = new JSONObject(responseEntity.getBody());
        double exchangeRate = responseJson.getJSONObject("rates").getDouble(to);

        // Calculate the converted amount
        double convertedAmount = amount * exchangeRate;

        // Pass the result to the view
        model.addAttribute("convertedAmount", convertedAmount);
        return "currency-converter";
    }
}


-----------------------------------------------------------------------------------------------------------------------------------------------
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Currency Converter</title>
</head>
<body>
    <h1>Currency Converter</h1>
    <form method="post" action="/convert">
        <label for="from">From Currency:</label>
        <input type="text" name="from" required>
        <br>
        <label for="to">To Currency:</label>
        <input type="text" name="to" required>
        <br>
        <label for="amount">Amount:</label>
        <input type="number" name="amount" required>
        <br>
        <input type="submit" value="Convert">
    </form>
    <br>
    <p th:if="${convertedAmount != null}" th:text="'Converted Amount: ' + ${convertedAmount}"></p>
</body>
</html>
---------------------------------------------------------------------------------------------------------------------------------------------------
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
