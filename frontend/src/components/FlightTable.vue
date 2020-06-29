<template>
    <div>
        <el-form :inline="true" ref="form" :model="form" label-width="50px">
            <el-form-item label="出发">
                <el-input v-model="form.depart" placeholder="输入出发地"></el-input>
            </el-form-item>
            <el-form-item label="到达">
                <el-input v-model="form.dest" placeholder="输入目的地"></el-input>
            </el-form-item>
            <el-form-item label="日期">
                <el-date-picker type="date" placeholder="选择日期" value-format="yyyy-MM-dd" v-model="form.date"
                                style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="flightID" label="航班编号" width="180">
            </el-table-column>
            <el-table-column prop="flightCompany" label="航空公司" width="180">
            </el-table-column>
            <el-table-column prop="departure" label="出发地" width="180">
            </el-table-column>
            <el-table-column prop="departureTime" label="出发时间" width="180">
            </el-table-column>
            <el-table-column prop="arrival" label="到达地" width="180">
            </el-table-column>
            <el-table-column prop="arrivalTime" label="到达时间" width="180">
            </el-table-column>
            <el-table-column prop="price" label="价格" width="180">
            </el-table-column>
            <el-table-column prop="rest" label="余量" width="180">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleBuy(scope.$index, scope.row)">购买</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="购买订单" :visible.sync="dialogFormVisible">
            <el-form :model="formBuy">
                <el-form-item label="航班编号" :label-width="formLabelWidth">
                    {{ formBuy.flightID }}
                </el-form-item>
                <el-form-item label="航空公司" :label-width="formLabelWidth">
                    {{ formBuy.flightCompany }}
                </el-form-item>
                <el-form-item label="出发地" :label-width="formLabelWidth">
                    {{ formBuy.departure }}
                </el-form-item>
                <el-form-item label="出发时间" :label-width="formLabelWidth">
                    {{ formBuy.departureTime }}
                </el-form-item>
                <el-form-item label="到达地" :label-width="formLabelWidth">
                    {{ formBuy.arrival }}
                </el-form-item>
                <el-form-item label="到达时间" :label-width="formLabelWidth">
                    {{ formBuy.arrivalTime }}
                </el-form-item>
                <el-form-item label="价格" :label-width="formLabelWidth">
                    {{ formBuy.price }}
                </el-form-item>
                <el-form-item label="余量" :label-width="formLabelWidth">
                    {{ formBuy.rest }}
                </el-form-item>
                <el-form-item label="支付方式" :label-width="formLabelWidth">
                    <el-select v-model="formBuy.payMethod" placeholder="请选择支付方式">
                        <el-option label="工商银行" value="ICBC"></el-option>
                        <el-option label="支付宝" value="Alipay"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="buyTicket()">购 买</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: 'FlightTable',
        data: function () {
            return {
                form: {
                    depart: '',
                    dest: '',
                    date: ''
                },
                tableData: [],
                loading: true,
                dialogFormVisible: false,
                formBuy: {
                    flightID: '',
                    flightCompany: '',
                    departure: '',
                    departureTime: '',
                    arrival: '',
                    arrivalTime: '',
                    price: '',
                    rest: '',
                    payMethod: ''
                },
                formLabelWidth: '120px'
            }
        },
        mounted() {
            this.onSearch();
        },
        methods: {
            buyTicket() {
                const that = this;
                axios.get('api/buyFlight', {
                    params: {
                        "flightID": this.formBuy.flightID,
                        "flightCompany": this.formBuy.flightCompany,
                        "payMethod": this.formBuy.payMethod
                    }
                }).then(function (ret) {
                    if (ret.data.data.key) {
                        that.onSearch();
                        that.$message({
                            message: ret.data.data.value,
                            type: 'success'
                        });
                    }
                    else
                        that.$message.error(ret.data.data.value);
                    //alert(ret.data.data.value);
                }, function () {
                    console.log('传输失败');
                });
                this.dialogFormVisible = false;
            },
            handleBuy(index, row) {
                this.formBuy=row;
                this.formBuy.payMethod="ICBC";
                this.dialogFormVisible = true;
            },
            onSearch() {
                const that = this;
                if (this.form.date == null) this.form.date = '';
                axios.get('api/getFlight', {
                    params: this.form
                }).then(function (ret) {
                    console.log(ret)
                    if (ret.data.status === 0) {
                        that.tableData = ret.data.data;
                    }
                }, function () {
                    console.log('传输失败');
                });
            }
        }
    }
</script>
