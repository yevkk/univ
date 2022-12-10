using System.ComponentModel.DataAnnotations.Schema;

namespace DDB_CW.Models
{
    public class Employee
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("fullname")]
        public string? Fullname { get; set; }

        [Column("email")]
        public string? Email { get; set; }

        [Column("department")]
        public string? Department { get; set; }

        [Column("position")]
        public string? Position { get; set; }

        [Column("hire_year")]
        public int? HireYear { get; set; }

        public string Desc => string.Format("{0}, {1}", Fullname, Email);
    }
}
