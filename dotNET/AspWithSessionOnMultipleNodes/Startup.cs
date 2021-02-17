using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.AspNetCore.DataProtection;
using Microsoft.AspNetCore.DataProtection.StackExchangeRedis;
using StackExchange.Redis;

namespace app
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
			// allows multiple instance of the same application
            services.AddDataProtection()
                .SetApplicationName("myapp_session")
                .PersistKeysToStackExchangeRedis(ConnectionMultiplexer.Connect("redis:6379,password=cE3nNEXHmvGCwdqxxxxxxxxxxx"),
                    "DataProtection-Keys");

            services.AddControllersWithViews();

			// distributed shared cache
            services.AddStackExchangeRedisCache(action => {
                action.InstanceName = "redis";
                action.Configuration = "redis:6379,password=cE3nNEXHmvGCwdqxxxxxxxxxxx";
            });

            services.AddSession(options => {
                options.Cookie.Name = "myapp_session";
                options.IdleTimeout = TimeSpan.FromMinutes(60 * 24);
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseSession();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=Home}/{action=Index}/{id?}");
            });
        }
    }
}
