using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace DDBLab6.Models
{
    public class Student
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("name")]
        public string? Name { get; set; }

        [Column("phone")]
        public string? Phone { get; set; }

        [Column("practice_hours")]
        [Display(Name = "Practice Hours")]
        public int PracticeHours { get; set; }

        [Column("theory_exam_pass")]
        [Display(Name = "Theory Exam Pass")]
        public bool TheoryExamPass { get; set; }

        [Column("practical_exam_attempts")]
        [Display(Name = "Practical Exam Attempts")]
        public int PracticalExamAttempts { get; set; }

        [Column("practical_exam_pass")]
        [Display(Name = "Practical Exam Pass")]
        public bool PracticalExamPass { get; set; }

        [Column("dedicated_instructor_id")]
        [Display(Name = "Dedicated Instructor")]
        public int? DedicatedInstructorId { get; set; }

        [Column("group_id")]
        [Display(Name = "Group No.")]
        public int? GroupId { get; set; }
    }
}
