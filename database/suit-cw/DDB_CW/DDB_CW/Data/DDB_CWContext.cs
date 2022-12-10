using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using DDB_CW.Models;

namespace DDB_CW.Data
{
    public class DDB_CWContext : DbContext
    {
        public DDB_CWContext (DbContextOptions<DDB_CWContext> options)
            : base(options)
        {
        }

        public DbSet<DDB_CW.Models.Hall> halls { get; set; } = default!;

        public DbSet<DDB_CW.Models.Row> rows { get; set; } = default!;
    }
}
