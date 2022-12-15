package rs.enterprise.paymentserviceprovider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.enterprise.paymentserviceprovider.util.jwt.JwtFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtTokenFilter;
    private final PasswordEncoder passwordEncoder;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtFilter jwtTokenFilter,
                          PasswordEncoder passwordEncoder,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.passwordEncoder = passwordEncoder;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/js/**", "/css/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);

        http.authorizeRequests()

                //api
                .antMatchers(HttpMethod.POST, "/api/users/authenticate/first-step").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/authenticate/second-step").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/bank-payment/request-redirect-to-bank/{merchantOrderId}/{bankPaymentId}/{method}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/bank-payment/request-redirect").permitAll()
                .antMatchers(HttpMethod.POST, "/api/bank-payment/final-redirect").permitAll()
                .antMatchers(HttpMethod.GET, "/api/payment-methods/{merchantId}/all").permitAll()
                .anyRequest().fullyAuthenticated();

        //TODO: ovo vidi
        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");

        http.addFilterBefore(jwtTokenFilter,
                BasicAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
