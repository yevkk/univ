using System;
using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;

namespace Lab2.Models
{
    [Keyless]
    public class QueryResult1
    {
        public string username { get; set; }
        public string fullname { get; set; }
    }

    [Keyless]
    public class QueryResult2
    {
        public string c_model { get; set; }
        public string l_model { get; set; }
    }

    [Keyless]
    public class QueryResult3
    {
        public string manufacturer { get; set; }
        public string model { get; set; }
    }

    [Keyless]
    public class QueryResult4
    {
        public string username { get; set; }
    }
}
