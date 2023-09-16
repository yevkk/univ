using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDB_CW.Models
{
    public class Booking
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("place")]
        public int? Place { get; set; }

        [Column("start_ts")]
        [Display(Name = "From")]
        public DateTime StartTs { get; set; }

        [Column("end_ts")]
        [Display(Name = "End")]
        public DateTime EndTs { get; set; }

        [Column("comment")]
        public string? Comment { get; set; }

        [ForeignKey("Employee")]
        [Column("employee_id")]
        [Display(Name = "Employee")]
        public int EmployeeId { get; set; }

        public Employee? Employee { get; set; }

        [ForeignKey("Row")]
        [Column("row_id")]
        [Display(Name = "Row")]
        public int RowId { get; set; }

        public Row? Row { get; set; }

        public string Desc => string.Format("{0}: {1}; {2}; from {3}, to {4}", Row != null ? Row.Desc : "null", Place, Employee != null ? Employee.Desc : "null", StartTs.ToString(), EndTs.ToString());
    }
}
