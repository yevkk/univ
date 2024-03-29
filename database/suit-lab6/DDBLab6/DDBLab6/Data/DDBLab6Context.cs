﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using DDBLab6.Models;

namespace DDBLab6.Data
{
    public class DDBLab6Context : DbContext
    {
        public DDBLab6Context (DbContextOptions<DDBLab6Context> options)
            : base(options)
        {
        }

        public DbSet<DDBLab6.Models.Car> cars { get; set; } = default!;

        public DbSet<DDBLab6.Models.Lecturer> lecturers { get; set; } = default!;

        public DbSet<DDBLab6.Models.Group> groups { get; set; } = default!;

        public DbSet<DDBLab6.Models.Instructor> instructors { get; set; } = default!;

        public DbSet<DDBLab6.Models.Student> students { get; set; } = default!;
    }
}
