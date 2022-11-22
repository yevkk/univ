using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDBLab6.Models
{
    public class Group
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("lectures_completed")]
        [Display(Name = "Lectures Completed")]
        public int LecturesCompleted { get; set; }

        [ForeignKey("Lecturer")]
        [Column("lecturer_id")]
        [Display(Name = "Lecturer")]
        public int? LecturerId { get; set; }

        public Lecturer? Lecturer { get; set; }
    }
}
